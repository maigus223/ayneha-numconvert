// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 MAIGUS

package com.maigus.ayneha.converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.maigus.ayneha.converter.navigation.AppNavHost
import com.maigus.ayneha.converter.ui.theme.AppBg
import com.maigus.ayneha.converter.ui.theme.AynehaConvertisseurTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AynehaConvertisseurTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AppBg) {
                    AppNavHost()
                }
            }
        }
        // Mise à jour depuis ayneha.org : tirer l'écran principal vers le bas (voir MiseAJourUi.kt).
        maj = MiseAJourUi(
            this,
            accent = 0xFFFFC933.toInt(),
            fond = 0xFF12151C.toInt(),
            encre = 0xFFFFFFFF.toInt(),
            police = androidx.core.content.res.ResourcesCompat.getFont(this, R.font.ayneha_pro),
            geste = false
        )
        MiseAJourUi.courant = maj
    }

    private lateinit var maj: MiseAJourUi

    override fun onResume() {
        super.onResume()
        if (::maj.isInitialized) { MiseAJourUi.courant = maj; maj.surReprise() }
    }

    override fun onDestroy() {
        if (MiseAJourUi.courant === maj) MiseAJourUi.courant = null
        super.onDestroy()
    }
}
