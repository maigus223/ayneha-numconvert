package com.maigus.ayneha.converter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maigus.ayneha.converter.data.DIGIT_MAPS
import com.maigus.ayneha.converter.data.Script
import com.maigus.ayneha.converter.data.keypadOrder
import com.maigus.ayneha.converter.ui.theme.AppGold
import com.maigus.ayneha.converter.ui.theme.AppCream
import com.maigus.ayneha.converter.ui.theme.AppPanel
import com.maigus.ayneha.converter.ui.theme.AppPanelLine
import com.maigus.ayneha.converter.ui.theme.fontFamilyFor

@Composable
fun Keypad(
    script: Script,
    onDigit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val order = keypadOrder(script)
    val map = DIGIT_MAPS.getValue(script)
    val font = fontFamilyFor(script)
    val fontSize = if (script == Script.FR) 20.sp else 23.sp
    val textColor = if (script == Script.AYNEHA) AppGold else AppCream

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        order.chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { digit ->
                    if (digit == null) {
                        Box(modifier = Modifier.weight(1f))
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(55.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AppPanel)
                                .border(1.dp, AppPanelLine, RoundedCornerShape(10.dp))
                                .clickable { onDigit(digit) }
                                .semantics { contentDescription = "Chiffre $digit" }
                        ) {
                            Text(map[digit], color = textColor, fontSize = fontSize, fontFamily = font)
                        }
                    }
                }
            }
        }
    }
}
