package com.maigus.ayneha.converter.data

/**
 * Les 4 systèmes numériques gérés par le convertisseur.
 */
enum class Script {
    AYNEHA, FR, AR, NKO, DOGON;

    val isRtl: Boolean
        get() = this == AYNEHA || this == NKO || this == DOGON
}

/**
 * Glyphes 0-9 pour chaque script.
 * AYNEHA utilise la zone d'usage privé (PUA) de la police "Ayneha Type" (U+E000-U+E009).
 */
val DIGIT_MAPS: Map<Script, List<String>> = mapOf(
    Script.AYNEHA to listOf(
        "\uE000", "\uE001", "\uE002", "\uE003", "\uE004",
        "\uE005", "\uE006", "\uE007", "\uE008", "\uE009"
    ),
    Script.FR to listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"),
    Script.AR to listOf(
        "\u0660", "\u0661", "\u0662", "\u0663", "\u0664",
        "\u0665", "\u0666", "\u0667", "\u0668", "\u0669"
    ),
    Script.NKO to listOf(
        "\u07C0", "\u07C1", "\u07C2", "\u07C3", "\u07C4",
        "\u07C5", "\u07C6", "\u07C7", "\u07C8", "\u07C9"
    ),
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
    Script.DOGON to "\u2009"
)

/**
 * Disposition du pavé numérique, en 4 lignes de 3 colonnes (null = case vide).
 * Les 3 scripts RTL (AYNEHA, N'ko, Arabe) sont en miroir horizontal du Français (LTR).
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
    return if (script == Script.AYNEHA || script == Script.DOGON) formatted.reversed() else formatted}
