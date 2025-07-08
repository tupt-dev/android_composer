package com.tupt.audio_composer.navigation

import androidx.navigation.NavBackStackEntry
import com.tupt.audio_composer.model.SampleProducts

/**
 * Class containing all navigation routes and their associated titles for the application
 */
object AppRoute {
    const val HOME = "home"
    const val DETAIL = "detail/{productId}"
    const val SETTINGS = "settings"
    // New routes from NavigationScreen
    const val COMPOSER = "composer"
    const val LIBRARY = "library"
    const val RECORD = "record"

    // Helper function to create detail route with product ID
    fun detailRoute(productId: String) = "detail/$productId"

    fun getTitle(route: String?): String {
        return when {
            route == HOME -> "Audio Composer"
            route == SETTINGS -> "Settings"
            route == COMPOSER -> "Composer"
            route == LIBRARY -> "Library"
            route == RECORD -> "Record"
            route == DETAIL || route?.startsWith("detail/") == true -> "Product Details"
            else -> "Audio Composer"
        }
    }

    fun getTitle(navBackStackEntry: NavBackStackEntry?): String {
        val route = navBackStackEntry?.destination?.route

        return when (route) {
            HOME -> "Audio Composer"
            SETTINGS -> "Settings"
            COMPOSER -> "Composer"
            LIBRARY -> "Library"
            RECORD -> "Record"
            DETAIL -> {
                // For detail screen, extract the productId from arguments
                val productId = navBackStackEntry.arguments?.getString("productId")
                if (productId != null) {
                    SampleProducts.getProductById(productId)?.name ?: "Product Details"
                } else {
                    "Product Details"
                }
            }
            else -> "Audio Composer"
        }
    }
}
