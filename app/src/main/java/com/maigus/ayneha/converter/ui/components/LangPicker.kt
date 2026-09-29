package com.maigus.ayneha.converter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maigus.ayneha.converter.R
import com.maigus.ayneha.converter.data.Script
import com.maigus.ayneha.converter.ui.theme.*

private data class LangOption(val script: Script, val label: String)

private val OPTIONS = listOf(
    LangOption(Script.AYNEHA, "AYNEHA"),
    LangOption(Script.NKO, "N'ko"),
    LangOption(Script.DOGON, "Dogon NÈNÈ"),
    LangOption(Script.AR, "Arabe"),
    LangOption(Script.FR, "Français")
)

@Composable
fun LangBadge(script: Script, tint: Color = AppGold, fontSize: androidx.compose.ui.unit.TextUnit = 18.sp) {
    when (script) {
        Script.AYNEHA -> Image(
            painter = painterResource(R.drawable.ayneha_word),
            contentDescription = "AYNEHA écrit en AYNEHA",
            modifier = Modifier.height(with(androidx.compose.ui.platform.LocalDensity.current) { fontSize.toDp() })
        )
        Script.NKO -> Text("ߒߞߏ", color = tint, fontSize = fontSize, fontFamily = NkoFontFamily)
        Script.AR -> Text("عربي", color = tint, fontSize = fontSize, fontFamily = ArabicFontFamily)
        Script.FR -> Text("Français", color = tint, fontSize = fontSize, fontFamily = BodyFontFamily)
		Script.DOGON -> Text("\uE34C\uE244\uE34C\uE244", color = tint, fontSize = fontSize, fontFamily = NeneFontFamily)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LangPicker(
    selected: Script,
    onSelect: (Script) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Text(
                "Clavier en",
                color = AppCreamDim,
                fontSize = 11.sp,
                fontFamily = BodyFontFamily
            )
        }

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppPanel)
                    .border(1.dp, AppPanelLine, RoundedCornerShape(10.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        OPTIONS.first { it.script == selected }.label,
                        color = AppCream,
                        fontSize = 14.sp,
                        fontFamily = BodyFontFamily
                    )
                    if (selected != Script.FR) LangBadge(selected, fontSize = 15.sp)
                }
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = AppCyan)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(AppBg)
            ) {
                OPTIONS.forEach { option ->
                    val isSelected = option.script == selected
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(option.label, color = AppCreamDim, fontFamily = BodyFontFamily, fontSize = 14.sp)
                                LangBadge(option.script, fontSize = 17.sp)
                            }
                        },
                        onClick = {
                            onSelect(option.script)
                            expanded = false
                        },
                        modifier = Modifier.background(
                            if (isSelected) AppCyan.copy(alpha = 0.14f) else Color.Transparent
                        )
                    )
                }
            }
        }
    }
}
