package com.maigus.ayneha.converter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maigus.ayneha.converter.ui.theme.*

@Composable
fun PrivacyScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(AppBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Retour",
                        tint = AppCyan
                    )
                }
                Text(
                    "Confidentialité",
                    color = AppCream,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = DisplayFontFamily
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                PrivacyParagraph(
                    "AYNEHA NumConvert est un outil de conversion de chiffres entre " +
                        "cinq systèmes numériques : l'écriture AYNEHA (Songhay), le Dogon NÈNÈ, " +
                        "le français, l'arabe (chiffres indo-arabes orientaux) et le N'ko. Cette " +
                        "page explique simplement ce que l'application fait (et surtout, ce " +
                        "qu'elle ne fait pas) avec vos informations."
                )

                SectionTitle("Aucune donnée collectée")
                PrivacyParagraph(
                    "L'application fonctionne entièrement sur votre appareil. Les nombres que " +
                        "vous saisissez pour les convertir ne sont ni enregistrés, ni envoyés vers " +
                        "un serveur, ni partagés avec qui que ce soit. Ils disparaissent dès que " +
                        "vous quittez ou réinitialisez l'application."
                )

                SectionTitle("Aucun compte, aucun suivi")
                PrivacyParagraph(
                    "AYNEHA NumConvert ne demande aucune création de compte, n'intègre " +
                        "aucun outil d'analyse d'audience, aucun traceur publicitaire et n'affiche " +
                        "aucune publicité."
                )

                SectionTitle("Permissions de l'application")
                PrivacyParagraph(
                    "L'application ne demande aucune permission particulière (pas d'accès à vos " +
                        "contacts, votre position, votre appareil photo ou vos fichiers). Certaines " +
                        "polices d'écriture (pour l'arabe et le N'ko) peuvent être téléchargées une " +
                        "seule fois par les services Google Play, indépendamment de l'application, " +
                        "puis restent mises en cache sur votre appareil."
                )

                SectionTitle("Utilisation par des enfants")
                PrivacyParagraph(
                    "Comme aucune donnée personnelle n'est collectée, l'application peut être " +
                        "utilisée sans risque particulier par un public jeune, dans un cadre " +
                        "pédagogique par exemple."
                )

                SectionTitle("Modifications de cette politique")
                PrivacyParagraph(
                    "Cette politique de confidentialité peut être mise à jour si l'application " +
                        "évolue. La version en vigueur est toujours celle affichée directement " +
                        "dans l'application."
                )
				
				SectionTitle("Logiciel libre")
                PrivacyParagraph(
                    "AYNEHA NumConvert est un logiciel libre, développé par MAIGUS avec " +
                        "l'aide d'outils d'IA (Claude, Anthropic). Il est publié sous licence " +
                        "GPL v3 ou ultérieure : vous pouvez l'utiliser, l'étudier, le modifier " +
                        "et le partager. Toute version modifiée distribuée doit rester libre, " +
                        "sous la même licence. Polices : SIL Open Font License 1.1. Logos et " +
                        "images : CC BY-SA 4.0. Code source : https://github.com/maigus223/ayneha-numconvert"
                )
                
				SectionTitle("Contact")
                PrivacyParagraph(
                    "Pour toute question concernant cette politique ou l'application, vous " +
                        "pouvez écrire à l'adresse indiquée ci-dessous."
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Développé par MAIGUS : gwokmt2q@duck.com",
                    color = AppGoldSoft,
                    fontSize = 12.sp,
                    fontFamily = BodyFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        color = AppGold,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = BodyFontFamily,
        modifier = Modifier.padding(top = 18.dp, bottom = 6.dp)
    )
}

@Composable
private fun PrivacyParagraph(text: String) {
    Text(
        text,
        color = AppCreamDim,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
        fontFamily = BodyFontFamily
    )
}
