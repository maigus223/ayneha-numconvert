package com.maigus.ayneha.converter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AppColorScheme = darkColorScheme(
    background = AppBg,
    surface = AppPanel,
    primary = AppGold,
    secondary = AppCyan,
    onBackground = AppCream,
    onSurface = AppCream,
    onPrimary = AppBg,
    onSecondary = AppBg,
    outline = AppPanelLine
)

@Composable
fun AynehaConvertisseurTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val context = view.context
        val window = (context as? android.app.Activity)?.window
        window?.let {
            it.statusBarColor = AppBg.toArgb()
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
