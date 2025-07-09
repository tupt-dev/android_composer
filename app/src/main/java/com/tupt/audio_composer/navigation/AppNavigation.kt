package com.tupt.audio_composer.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tupt.audio_composer.screens.DetailScreen
import com.tupt.audio_composer.screens.HomeScreen
import com.tupt.audio_composer.screens.MarketScreen
import com.tupt.audio_composer.screens.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.HOME,
        modifier = modifier
    ) {
        composable(AppRoute.HOME) {
            HomeScreen(navController)
        }

        composable(
            route = AppRoute.DETAIL,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) {
            DetailScreen(navController)
        }

        composable(AppRoute.SETTINGS) {
            SettingsScreen(navController)
        }

        composable(AppRoute.MARKET) {
            MarketScreen(navController)
        }
    }
}
