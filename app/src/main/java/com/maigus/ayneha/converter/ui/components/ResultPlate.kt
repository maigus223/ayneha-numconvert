package com.maigus.ayneha.converter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maigus.ayneha.converter.data.Script
import com.maigus.ayneha.converter.data.formatDigits
import com.maigus.ayneha.converter.ui.theme.*

private val LABELS = mapOf(
    Script.AYNEHA to "AYNEHA",
    Script.FR to "Français",
    Script.AR to "Arabe",
    Script.NKO to "N'ko",
    Script.DOGON to "Dogon NÈNÈ"
)

@Composable
fun ResultPlate(script: Script, value: String, modifier: Modifier = Modifier) {
    var flash by remember { mutableStateOf(false) }
    LaunchedEffect(value) {
        flash = true
        kotlinx.coroutines.delay(220)
        flash = false
    }
    val borderColor by animateColorAsState(
        targetValue = if (flash) AppCyanSoft else AppPanelLine,
        animationSpec = tween(180),
        label = "plateBorder"
    )

    val font = fontFamilyFor(script)
    val textColor = if (script == Script.AYNEHA) AppGold else AppCream
    val isRtl = script.isRtl
    val scroll = rememberScrollState()

    CompositionLocalProvider(
        LocalLayoutDirection provides
            if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppPanel)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp)
        ) {
            // Chiffres : du côté "start" (droite en RTL, gauche en LTR)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(scroll),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text("—", color = AppCreamDim, fontSize = 14.sp, fontFamily = BodyFontFamily)
                } else {
                    Text(
                        formatDigits(value, script),
                        color = textColor,
                        fontSize = 25.sp,
                        fontFamily = font,
                        style = TextStyle(
                            lineHeight = 32.sp,
                            textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr
                        )
                    )
                }
            }
            // Mention de la langue : du côté opposé aux chiffres
            Text(
                LABELS.getValue(script),
                color = AppCreamDim,
                fontSize = 15.6.sp,
                fontFamily = BodyFontFamily,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
