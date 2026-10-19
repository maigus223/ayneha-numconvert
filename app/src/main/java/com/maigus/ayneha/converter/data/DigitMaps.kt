// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 MAIGUS

package com.maigus.ayneha.converter.data

/**
 * Les 6 systèmes numériques gérés par le convertisseur.
 * Adlam (écriture du peul) : chiffres U+1E950-U+1E959, de droite à gauche comme le N'ko
 * (classe bidirectionnelle R : le premier chiffre du nombre est à droite).
 */
enum class Script {
    AYNEHA, FR, AR, NKO, ADLAM, DOGON;

    val isRtl: Boolean
        get() = this == AYNEHA || this == NKO || this == ADLAM || this == DOGON
}

/** Ordre d'affichage commun (sélecteur et résultats) : les écritures africaines d'abord. */
val SCRIPT_ORDER: List<Script> = listOf(Script.AYNEHA, Script.NKO, Script.ADLAM, Script.DOGON, Script.AR, Script.FR)

/** Point de code du chiffre 0 adlam (plan 1, U+1E950 ... U+1E959). */
const val ADLAM_DIGIT_ZERO = 0x1E950

/** Point de code du chiffre 0 AYNEHA (plan 16, U+10FF7C ... U+10FF85 pour 0-9). */
const val AYNEHA_DIGIT_ZERO = 0x10FF7C

/**
 * Glyphes 0-9 pour chaque script.
 * AYNEHA utilise le bloc plan 16 de la police "ayneha_pro" (U+10FF7C-U+10FF85, paires de substitution UTF-16).
 * Dogon NÈNÈ garde la zone d'usage privé de la police nene_regular (U+E01A-U+E023).
 */
val DIGIT_MAPS: Map<Script, List<String>> = mapOf(
    Script.AYNEHA to List(10) { String(Character.toChars(AYNEHA_DIGIT_ZERO + it)) },
    Script.FR to listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"),
    Script.AR to listOf(
        "\u0660", "\u0661", "\u0662", "\u0663", "\u0664",
        "\u0665", "\u0666", "\u0667", "\u0668", "\u0669"
    ),
    Script.NKO to listOf(
        "\u07C0", "\u07C1", "\u07C2", "\u07C3", "\u07C4",
        "\u07C5", "\u07C6", "\u07C7", "\u07C8", "\u07C9"
    ),
    Script.ADLAM to List(10) { String(Character.toChars(ADLAM_DIGIT_ZERO + it)) },
    Script.DOGON to listOf(
        "\uE023", "\uE01A", "\uE01B", "\uE01C", "\uE01D",
        "\uE01E", "\uE01F", "\uE020", "\uE021", "\uE022"
    )
)

/** Séparateur de milliers propre à chaque script (espace fine, ou séparateur arabe authentique). */
val SEPARATORS: Map<Script, String> = mapOf(
    Script.AYNEHA to "\u2009",
    Script.FR to "\u2009",
    Script.AR to "\u066C",
    Script.NKO to "\u2009",
    Script.ADLAM to "\u2009",
    Script.DOGON to "\u2009"
)

/**
 * Disposition du pavé numérique, en 4 lignes de 3 colonnes (null = case vide).
 * Les scripts RTL (AYNEHA, N'ko, Adlam, Dogon NÈNÈ) sont en miroir horizontal du Français (LTR).
 */
val KEYPAD_ORDER_RTL: List<Int?> = listOf(9, 8, 7, 6, 5, 4, 3, 2, 1, null, 0, null)
val KEYPAD_ORDER_LTR: List<Int?> = listOf(7, 8, 9, 4, 5, 6, 1, 2, 3, null, 0, null)

fun keypadOrder(script: Script): List<Int?> = if (script.isRtl) KEYPAD_ORDER_RTL else KEYPAD_ORDER_LTR

/**
 * Formate une chaîne de chiffres décimaux (ex. "1234567") dans le script demandé,
 * avec regroupement par milliers.
 */
fun formatDigits(value: String, script: Script): String {
    if (value.isEmpty()) return ""
    val map = DIGIT_MAPS.getValue(script)
    val sep = SEPARATORS.getValue(script)

    val groups = mutableListOf<String>()
    var s = value
    while (s.length > 3) {
        groups.add(0, s.takeLast(3))
        s = s.dropLast(3)
    }
    groups.add(0, s)

    val formatted = groups.joinToString(sep) { group ->
        group.map { ch -> map[ch - '0'] }.joinToString("")
    }
    return if (script == Script.AYNEHA || script == Script.DOGON) StringBuilder(formatted).reverse().toString() else formatted
}
