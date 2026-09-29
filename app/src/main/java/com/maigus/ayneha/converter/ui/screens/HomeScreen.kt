package com.maigus.ayneha.converter.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maigus.ayneha.converter.R
import com.maigus.ayneha.converter.data.Script
import com.maigus.ayneha.converter.data.formatDigits
import com.maigus.ayneha.converter.ui.components.Keypad
import com.maigus.ayneha.converter.ui.components.LangPicker
import com.maigus.ayneha.converter.ui.components.ResultPlate
import com.maigus.ayneha.converter.ui.theme.*

@Composable
fun HomeScreen(onOpenPrivacy: () -> Unit) {
    var value by remember { mutableStateOf("") }
    var lang by remember { mutableStateOf(Script.FR) }

    Box(modifier = Modifier.fillMaxSize().background(AppBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
                .padding(top = 14.dp, bottom = 40.dp)
        ) {
            // ---------- Ligne du "i", seule en haut ----------
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(
                    onClick = onOpenPrivacy,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Confidentialité",
                        tint = AppCyan
                    )
                }
            }

            // ---------- Titre, en dessous : AYNEHA puis latin, même largeur ----------
            var titleWidth by remember { mutableStateOf(0.dp) }
            val density = androidx.compose.ui.platform.LocalDensity.current
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (titleWidth > 0.dp) {
                    Image(
                        painter = painterResource(R.drawable.ayneha_full_name),
                        contentDescription = "AYNEHA barmey-ko",
                        modifier = Modifier
                            .width(titleWidth)
                            .aspectRatio(168f / 25f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Text(
                    "AYNEHA NumConvert",
                    color = AppCream,
                    fontSize = 19.2.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = DisplayFontFamily,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        titleWidth = with(density) { coordinates.size.width.toDp() }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---------- Sélecteur de langue ----------
            LangPicker(selected = lang, onSelect = { lang = it })

            Spacer(modifier = Modifier.height(12.dp))

            // ---------- Carte de saisie ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppPanel)
                    .border(1.dp, AppPanelLine, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text("Nombre saisi", color = AppCreamDim, fontSize = 11.sp, fontFamily = BodyFontFamily)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val isRtl = lang.isRtl
                    Box(modifier = Modifier.weight(1f)) {
                        androidx.compose.runtime.CompositionLocalProvider(
                            androidx.compose.ui.platform.LocalLayoutDirection provides
                                if (isRtl) androidx.compose.ui.unit.LayoutDirection.Rtl
                                else androidx.compose.ui.unit.LayoutDirection.Ltr
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (value.isEmpty()) {
                                    Text(
                                        com.maigus.ayneha.converter.data.DIGIT_MAPS.getValue(lang)[0],
                                        color = AppCreamDim,
                                        fontSize = 30.sp,
                                        fontFamily = fontFamilyFor(lang)
                                    )
                                } else {
                                    Text(
                                        formatDigits(value, lang),
                                        color = if (lang == Script.AYNEHA) AppGold else AppCream,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = fontFamilyFor(lang),
                                        style = androidx.compose.ui.text.TextStyle(
                                            textDirection = if (isRtl) androidx.compose.ui.text.style.TextDirection.Rtl
                                                else androidx.compose.ui.text.style.TextDirection.Ltr
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        RoundIconButton(onClick = { value = value.dropLast(1) }) {
                            Icon(Icons.Outlined.Backspace, contentDescription = "Effacer le dernier chiffre", tint = AppCreamDim, modifier = Modifier.size(16.dp))
                        }
                        RoundIconButton(onClick = { value = "" }) {
                            Text("C", color = AppCreamDim, fontSize = 13.sp, fontFamily = BodyFontFamily)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ---------- Pavé numérique ----------
            Keypad(
                script = lang,
                onDigit = { d ->
                    value = if (value == "0") d.toString()
                    else if (value.length >= 10) value
                    else value + d.toString()
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ---------- Résultats ----------
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(Script.AYNEHA, Script.NKO, Script.DOGON, Script.AR, Script.FR).forEach { s ->
                    ResultPlate(script = s, value = value)
                }
            }
        }

        Text(
            "By MAIGUS",
            color = AppGoldSoft,
            fontSize = 10.5.sp,
            fontFamily = BodyFontFamily,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 18.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun RoundIconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(9.dp))
            .border(1.dp, AppPanelLine, RoundedCornerShape(9.dp))
            .background(AppPanel)
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(34.dp)) {
            content()
        }
    }
}
