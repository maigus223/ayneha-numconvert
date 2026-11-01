// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 MAIGUS

package com.maigus.ayneha.converter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * Mise à jour depuis ayneha.org (en plus de Naabiga).
 *
 * Quand on tire l'écran vers le bas, l'application lit le petit fichier
 * https://ayneha.org/mises-a-jour.json (version, lien de l'APK et empreinte
 * SHA-256). Si une version plus récente existe, l'APK est téléchargé, son
 * empreinte est vérifiée, puis l'écran d'installation d'Android s'ouvre :
 * c'est toujours l'utilisateur qui confirme. Aucune donnée personnelle n'est
 * envoyée : seule cette lecture du site a lieu, et seulement à la demande.
 *
 * Le même fichier sert pour toutes les applications AYNEHA : seul ID change (ici ayneha-numconvert).
 */
object MiseAJour {

    private const val URL_LISTE = "https://ayneha.org/mises-a-jour.json"
    private const val ID = "ayneha-numconvert"
    private const val DOSSIER = "maj"

    data class Info(val version: String, val apk: String, val sha256: String?)

    /** Version installée (versionName). */
    fun versionActuelle(context: Context): String =
        try { context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "0" } catch (e: Exception) { "0" }

    /** Compare deux versions « 2.1.2 » : négatif si a < b, 0 si égales, positif si a > b. */
    fun comparer(a: String, b: String): Int {
        val x = a.split('.', '-').map { it.toIntOrNull() ?: 0 }
        val y = b.split('.', '-').map { it.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(x.size, y.size)) {
            val d = x.getOrElse(i) { 0 } - y.getOrElse(i) { 0 }
            if (d != 0) return d
        }
        return 0
    }

    /** Lit mises-a-jour.json sur ayneha.org (à appeler hors du fil principal). */
    fun chercher(): Info? {
        val cx = URL("$URL_LISTE?t=${System.currentTimeMillis() / 60000}").openConnection() as HttpURLConnection
        cx.connectTimeout = 10000; cx.readTimeout = 15000
        cx.setRequestProperty("Cache-Control", "no-cache")
        try {
            if (cx.responseCode != 200) throw Exception("HTTP ${cx.responseCode}")
            val texte = cx.inputStream.bufferedReader().use { it.readText() }
            val liste = JSONObject(texte).getJSONArray("applications")
            for (i in 0 until liste.length()) {
                val a = liste.getJSONObject(i)
                if (a.optString("id") == ID) {
                    val apk = a.optString("apk")
                    if (!apk.startsWith("https://")) return null
                    return Info(a.optString("version"), apk, a.optString("sha256").ifEmpty { null })
                }
            }
            return null
        } finally { cx.disconnect() }
    }

    private fun fichierApk(context: Context, info: Info): File =
        File(File(context.cacheDir, DOSSIER).apply { mkdirs() }, "ayneha-numconvert-${info.version}.apk")

    /** Télécharge l'APK et vérifie son empreinte. [progres] reçoit 0..100. */
    fun telecharger(context: Context, info: Info, progres: (Int) -> Unit): File {
        val dossier = File(context.cacheDir, DOSSIER).apply { mkdirs() }
        dossier.listFiles()?.forEach { it.delete() }   // anciens téléchargements
        val cible = fichierApk(context, info)
        val tmp = File(dossier, cible.name + ".part")
        val cx = URL(info.apk).openConnection() as HttpURLConnection
        cx.connectTimeout = 15000; cx.readTimeout = 30000
        val sha = MessageDigest.getInstance("SHA-256")
        try {
            if (cx.responseCode != 200) throw Exception("HTTP ${cx.responseCode}")
            val total = cx.contentLengthLong
            var lu = 0L; var dernier = -1
            cx.inputStream.use { ins ->
                tmp.outputStream().use { out ->
                    val buf = ByteArray(64 * 1024)
                    while (true) {
                        val n = ins.read(buf); if (n < 0) break
                        out.write(buf, 0, n); sha.update(buf, 0, n); lu += n
                        if (total > 0) { val p = (lu * 100 / total).toInt(); if (p != dernier) { dernier = p; progres(p) } }
                    }
                }
            }
        } finally { cx.disconnect() }
        val empreinte = sha.digest().joinToString("") { "%02x".format(it) }
        if (info.sha256 != null && !empreinte.equals(info.sha256, ignoreCase = true)) {
            tmp.delete()
            throw SecurityException("Empreinte SHA-256 différente : fichier refusé")
        }
        if (!tmp.renameTo(cible)) { tmp.copyTo(cible, overwrite = true); tmp.delete() }
        return cible
    }

    /** Vrai si Android autorise déjà cette application à installer des APK. */
    fun peutInstaller(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.O || context.packageManager.canRequestPackageInstalls()

    /** Ouvre le réglage « Installer des applications inconnues » pour cette application. */
    fun ouvrirAutorisation(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${context.packageName}"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    /** Ouvre l'écran d'installation d'Android (l'utilisateur confirme). */
    fun installer(context: Context, apk: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apk)
        val i = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(i)
    }

    /** APK déjà téléchargé et vérifié pour cette version, s'il existe. */
    fun dejaTelecharge(context: Context, info: Info): File? = fichierApk(context, info).takeIf { it.isFile && it.length() > 0 }
}
