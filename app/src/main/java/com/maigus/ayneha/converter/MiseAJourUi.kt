// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 MAIGUS

package com.maigus.ayneha.converter

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import java.io.File
import kotlin.math.abs
import kotlin.math.min

/**
 * Interface de la mise à jour depuis ayneha.org (écran natif) :
 *  - tirer l'écran vers le bas fait descendre un rond orange avec une flèche ;
 *  - en lâchant, l'application cherche une nouvelle version (MiseAJour.kt) ;
 *  - une fenêtre en icônes et en chiffres AYNEHA montre l'état, la progression
 *    du téléchargement et un bouton orange pour télécharger.
 * Aucun texte latin : uniquement des icônes et le numéro de version en AYNEHA.
 *
 * Utilisation dans l'activité : créer MiseAJourUi(this, ...) après setContentView,
 * transmettre les événements tactiles (dispatchTouchEvent -> surToucher) et
 * appeler surReprise() dans onResume().
 */
class MiseAJourUi(
    private val act: Activity,
    private val accent: Int,
    private val fond: Int,
    private val encre: Int,
    private val police: Typeface?,
    /** Part de l'écran (depuis le haut) où le geste peut commencer : 1 = tout l'écran. */
    private val zoneHaute: Float = 1f,
    /** false : pas de geste propre (l'application a déjà son « tirer pour actualiser »). */
    private val geste: Boolean = true
) {
    private val dp = act.resources.displayMetrics.density
    private val principal = Handler(Looper.getMainLooper())
    private var info: MiseAJour.Info? = null
    @Volatile private var enAttente: File? = null
    private var enCours = false

    private val tailleRond = (44 * dp).toInt()
    private val indicateur = ImageView(act).apply {
        setImageResource(R.drawable.ic_maj_tirer)
        val p = (10 * dp).toInt(); setPadding(p, p, p, p)
        layoutParams = FrameLayout.LayoutParams(tailleRond, tailleRond, Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        alpha = 0f; translationY = -tailleRond.toFloat(); elevation = 8 * dp
    }

    private var dialog: Dialog? = null
    private lateinit var ico: ImageView
    private lateinit var ver: TextView
    private lateinit var barre: ProgressBar
    private lateinit var bouton: ImageButton

    init {
        styleRond(false)
        if (geste) act.findViewById<ViewGroup>(android.R.id.content).addView(indicateur)
    }

    private fun styleRond(pret: Boolean) {
        indicateur.background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(if (pret) accent else fond)
            setStroke((1.5f * dp).toInt(), accent)
        }
        indicateur.setColorFilter(if (pret) Color.WHITE else accent)
    }

    // ------------------------------------------------------------ geste
    private var y0 = -1f
    private var x0 = 0f
    private var dy = 0f
    private val seuil = 110 * dp

    /** À appeler depuis Activity.dispatchTouchEvent (ne consomme pas l'événement). */
    fun surToucher(ev: MotionEvent) {
        if (!geste) return
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val h = act.resources.displayMetrics.heightPixels
                if (ev.rawY > h * zoneHaute) { y0 = -1f; return }
                y0 = ev.rawY; x0 = ev.rawX; dy = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                if (y0 < 0 || dialog?.isShowing == true) return
                dy = ev.rawY - y0
                val dx = abs(ev.rawX - x0)
                if (dy <= 0 || dx > dy) { indicateur.alpha = 0f; return }
                val d = min(dy, seuil * 1.3f)
                indicateur.alpha = min(1f, d / seuil)
                indicateur.translationY = d * 0.6f - tailleRond
                indicateur.rotation = d / dp * 2
                styleRond(dy >= seuil)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val declencher = ev.actionMasked == MotionEvent.ACTION_UP && y0 >= 0 && dy >= seuil
                y0 = -1f; dy = 0f
                indicateur.animate().alpha(0f).translationY(-tailleRond.toFloat()).setDuration(180).start()
                styleRond(false)
                if (declencher) verifier()
            }
        }
    }

    // ------------------------------------------------------------ geste fourni par l'écran
    // Pour une interface Compose (liste qui défile) : l'écran mesure lui-même la
    // distance tirée quand la liste est tout en haut, et appelle tirer() puis lacher().
    private var tire = 0f

    /** Distance tirée vers le bas (en pixels) : déplace et anime le rond orange. */
    fun tirer(distancePx: Float) {
        if (indicateur.parent == null) act.findViewById<ViewGroup>(android.R.id.content).addView(indicateur)
        tire = distancePx
        val d = min(distancePx, seuil * 1.3f)
        indicateur.alpha = min(1f, d / seuil)
        indicateur.translationY = d * 0.6f - tailleRond
        indicateur.rotation = d / dp * 2
        styleRond(distancePx >= seuil)
    }

    /** Le doigt est levé : lance la recherche si on a tiré assez loin. */
    fun lacher() {
        val declencher = tire >= seuil
        tire = 0f
        indicateur.animate().alpha(0f).translationY(-tailleRond.toFloat()).setDuration(180).start()
        styleRond(false)
        if (declencher) verifier()
    }

    // ------------------------------------------------------------ fenêtre
    private fun fenetre(): Dialog {
        dialog?.let { return it }
        val d = Dialog(act)
        val boite = LinearLayout(act).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            val p = (24 * dp).toInt(); setPadding(p, p, p, p)
            background = GradientDrawable().apply { setColor(fond); cornerRadius = 22 * dp; setStroke((1 * dp).toInt(), 0x33FFFFFF) }
        }
        ico = ImageView(act).apply { setColorFilter(accent) }
        boite.addView(ico, LinearLayout.LayoutParams((60 * dp).toInt(), (60 * dp).toInt()))
        ver = TextView(act).apply {
            setTextColor(encre); textSize = 30f; gravity = Gravity.CENTER
            if (police != null) typeface = police
            setPadding(0, (12 * dp).toInt(), 0, (12 * dp).toInt())
        }
        boite.addView(ver, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        barre = ProgressBar(act, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100; progressTintList = android.content.res.ColorStateList.valueOf(accent)
            layoutDirection = android.view.View.LAYOUT_DIRECTION_RTL   // se remplit de droite à gauche
        }
        boite.addView(barre, LinearLayout.LayoutParams((220 * dp).toInt(), (10 * dp).toInt()).apply { bottomMargin = (12 * dp).toInt() })
        bouton = ImageButton(act).apply {
            setImageResource(R.drawable.ic_maj_telech); setColorFilter(Color.WHITE)
            background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(accent) }
            val p = (16 * dp).toInt(); setPadding(p, p, p, p)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setOnClickListener { visibility = android.view.View.GONE; telecharger() }
        }
        boite.addView(bouton, LinearLayout.LayoutParams((68 * dp).toInt(), (68 * dp).toInt()))
        d.setContentView(boite)
        d.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout((300 * dp).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog = d
        return d
    }

    /** Chiffres AYNEHA (Plan 16), affichés de droite à gauche. */
    private fun ayneha(s: String): String {
        val t = s.map { c -> if (c in '0'..'9') String(Character.toChars(0x10FF7C + (c - '0'))) else c.toString() }.joinToString("")
        return "‮$t‬"
    }

    private fun etat(e: String, version: String? = null, pct: Int = 0) = principal.post {
        val d = fenetre()
        if (!d.isShowing && !act.isFinishing) d.show()
        val res = when (e) {
            "verif" -> R.drawable.ic_maj_verif; "ajour" -> R.drawable.ic_maj_ajour
            "dispo" -> R.drawable.ic_maj_dispo; "telech" -> R.drawable.ic_maj_telech
            "pret" -> R.drawable.ic_maj_pret; "autoriser" -> R.drawable.ic_maj_autoriser
            "refuse" -> R.drawable.ic_maj_refuse; else -> R.drawable.ic_maj_erreur
        }
        ico.setImageResource(res)
        ico.animate().cancel(); ico.rotation = 0f
        if (e == "verif") ico.animate().rotationBy(3600f).setDuration(10000).setInterpolator(LinearInterpolator()).start()
        ver.text = when (e) { "ajour", "dispo" -> ayneha(version ?: ""); "telech" -> ayneha("$pct%"); else -> "" }
        ver.visibility = if (ver.text.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
        barre.visibility = if (e == "telech") android.view.View.VISIBLE else android.view.View.GONE
        barre.progress = pct
        bouton.visibility = if (e == "dispo" || e == "autoriser") android.view.View.VISIBLE else android.view.View.GONE
    }

    // ------------------------------------------------------------ actions
    private fun verifier() = verifier(false)

    /**
     * Cherche une nouvelle version. [discret] = true : la fenêtre ne s'ouvre que si une
     * nouvelle version existe (rien n'est montré si l'application est à jour ou hors ligne).
     */
    fun verifier(discret: Boolean) {
        if (enCours) return
        enCours = true
        if (!discret) etat("verif")
        Thread {
            try {
                val i = MiseAJour.chercher()
                val actuelle = MiseAJour.versionActuelle(act)
                if (i == null || MiseAJour.comparer(i.version, actuelle) <= 0) { if (!discret) etat("ajour", actuelle) }
                else { info = i; etat("dispo", i.version) }
            } catch (e: Exception) { if (!discret) etat("erreur") }
            enCours = false
        }.start()
    }

    private fun telecharger() {
        val i = info ?: return
        Thread {
            try {
                val f = MiseAJour.dejaTelecharge(act, i) ?: MiseAJour.telecharger(act, i) { p -> etat("telech", pct = p) }
                if (!MiseAJour.peutInstaller(act)) {
                    enAttente = f
                    etat("autoriser")
                    principal.post { MiseAJour.ouvrirAutorisation(act) }
                } else {
                    etat("pret")
                    principal.post { MiseAJour.installer(act, f) }
                }
            } catch (e: SecurityException) { etat("refuse")
            } catch (e: Exception) { etat("erreur") }
        }.start()
    }

    /** À appeler dans onResume() : reprend l'installation après l'autorisation. */
    fun surReprise() {
        val f = enAttente ?: return
        if (MiseAJour.peutInstaller(act)) { enAttente = null; etat("pret"); MiseAJour.installer(act, f) }
    }

    companion object {
        /** Instance de l'activité en cours (pour les écrans Compose). */
        @Volatile var courant: MiseAJourUi? = null
    }
}
