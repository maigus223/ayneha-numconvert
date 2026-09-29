package com.maigus.ayneha.converter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maigus.ayneha.converter.ui.screens.HomeScreen
import com.maigus.ayneha.converter.ui.screens.PrivacyScreen

private const val ROUTE_HOME = "home"
private const val ROUTE_PRIVACY = "privacy"

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_HOME) {
        composable(ROUTE_HOME) {
            HomeScreen(onOpenPrivacy = { navController.navigate(ROUTE_PRIVACY) })
        }
        composable(ROUTE_PRIVACY) {
            PrivacyScreen(onBack = { navController.popBackStack() })
        }
    }
}
