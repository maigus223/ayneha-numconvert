package com.maigus.ayneha.converter.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontSize = 15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontSize = 13.sp
    ),
    labelSmall = TextStyle(
        fontFamily = BodyFontFamily,
        fontSize = 11.sp
    )
)
