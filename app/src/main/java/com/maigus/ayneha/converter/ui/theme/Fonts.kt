package com.maigus.ayneha.converter.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font as GoogleDownloadableFont
import com.maigus.ayneha.converter.R

/** Police custom AYNEHA, embarquée dans l'application (res/font/ayneha_type.ttf). */
val AynehaFontFamily = FontFamily(Font(R.font.ayneha_type))
/** Police custom Dogon NÈNÈ, embarquée dans l'application (res/font/nene_regular.ttf). */
val NeneFontFamily = FontFamily(Font(R.font.nene_regular))

/**
 * Fournisseur Google Fonts (polices téléchargées via Google Play Services au premier lancement,
 * puis mises en cache par le système — aucune permission Internet requise côté application).
 */
private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private fun googleFontFamily(name: String, weight: FontWeight = FontWeight.Normal) = FontFamily(
    GoogleDownloadableFont(googleFont = GoogleFont(name), fontProvider = googleFontProvider, weight = weight)
)

/** Titre / en-tête (équivalent Fraunces du site). Repli automatique sur une police système serif. */
val DisplayFontFamily = googleFontFamily("Fraunces", FontWeight.Medium)

/** Corps de texte / interface (équivalent Work Sans). */
val BodyFontFamily = googleFontFamily("Work Sans")

/** Chiffres arabo-indiens orientaux. */
val ArabicFontFamily = googleFontFamily("Noto Naskh Arabic", FontWeight.SemiBold)

/** Chiffres N'ko. */
val NkoFontFamily = googleFontFamily("Noto Sans NKo", FontWeight.SemiBold)

/** Renvoie la police adaptée à un script donné, pour l'affichage des chiffres. */
fun fontFamilyFor(script: com.maigus.ayneha.converter.data.Script): FontFamily = when (script) {
    com.maigus.ayneha.converter.data.Script.AYNEHA -> AynehaFontFamily
    com.maigus.ayneha.converter.data.Script.AR -> ArabicFontFamily
    com.maigus.ayneha.converter.data.Script.NKO -> NkoFontFamily
    com.maigus.ayneha.converter.data.Script.FR -> BodyFontFamily
	com.maigus.ayneha.converter.data.Script.DOGON -> NeneFontFamily
}
