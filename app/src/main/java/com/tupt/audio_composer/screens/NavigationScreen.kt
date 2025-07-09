package com.tupt.audio_composer.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tupt.audio_composer.navigation.AppNavigation
import com.tupt.audio_composer.navigation.AppRoute
import com.tupt.audio_composer.viewmodel.SettingsViewModel

sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : NavigationItem(AppRoute.HOME, Icons.Default.Home, "Home")
    object Market : NavigationItem(AppRoute.MARKET, Icons.Default.ShoppingCart, "Market")
    object Settings : NavigationItem(AppRoute.SETTINGS, Icons.Default.Settings, "Settings")
}

@Composable
fun MainScreen(settingsViewModel: SettingsViewModel, context: Context) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom navigation on detail screen
    val showBottomBar = currentRoute != AppRoute.DETAIL && currentRoute?.startsWith("detail/") != true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            context = context,
            modifier = Modifier
                .padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp)
        )
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Market,
        NavigationItem.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    }
}