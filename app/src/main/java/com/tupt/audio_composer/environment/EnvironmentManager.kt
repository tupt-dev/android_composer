package com.tupt.audio_composer.environment

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import com.tupt.audio_composer.config.AppConfig

object EnvironmentManager {

    private const val TAG = "EnvironmentManager"

    // Environment types
    enum class Environment {
        DEVELOPMENT,
        STAGING,
        PRODUCTION;

        companion object {
            fun fromString(value: String): Environment {
                return when (value.uppercase()) {
                    "DEVELOPMENT" -> DEVELOPMENT
                    "STAGING" -> STAGING
                    "PRODUCTION" -> PRODUCTION
                    else -> DEVELOPMENT
                }
            }
        }
    }

    // Get current environment
    fun getCurrentEnvironment(): Environment {
        return Environment.fromString(AppConfig.ENVIRONMENT)
    }

    // Check if app is debuggable
    fun isDebuggable(context: Context): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    private fun initializeDevelopment(context: Context) {
        // Enable strict mode for development
        // enableStrictMode()

        // Initialize development-specific tools
        // initializeLeakCanary()
        // initializeFlipper()

        logEnvironmentInfo("Development environment initialized")
    }

    private fun initializeStaging(context: Context) {
        // Initialize staging-specific settings
        // initializeCrashlytics()
        // initializeAnalytics()

        logEnvironmentInfo("Staging environment initialized")
    }

    private fun initializeProduction(context: Context) {
        // Initialize production-specific settings
        // initializeCrashlytics()
        // initializeAnalytics()
        // disableDebugging()

        logEnvironmentInfo("Production environment initialized")
    }

    // Feature flags based on environment
    fun isFeatureEnabled(feature: Feature): Boolean {
        return when (getCurrentEnvironment()) {
            Environment.DEVELOPMENT -> feature.enabledInDev
            Environment.STAGING -> feature.enabledInStaging
            Environment.PRODUCTION -> feature.enabledInProduction
        }
    }

    // Utility functions
    fun shouldShowDebugInfo(): Boolean {
        return getCurrentEnvironment() != Environment.PRODUCTION
    }

    fun shouldUseSecureConnection(): Boolean {
        return getCurrentEnvironment() == Environment.PRODUCTION
    }

    fun getTimeoutMultiplier(): Float {
        return when (getCurrentEnvironment()) {
            Environment.DEVELOPMENT -> 2.0f // Longer timeout for development
            Environment.STAGING -> 1.5f
            Environment.PRODUCTION -> 1.0f
        }
    }

    private fun logEnvironmentInfo(message: String) {
        if (AppConfig.isDebugMode()) {
            Log.i(TAG, message)
        }
    }
}

// Environment configuration data class
data class EnvironmentConfig(
    val name: String,
    val baseUrl: String,
    val loggingEnabled: Boolean,
    val strictMode: Boolean,
    val mockData: Boolean,
    val analyticsEnabled: Boolean,
    val crashReportingEnabled: Boolean
)

// Feature flags
enum class Feature(
    val enabledInDev: Boolean,
    val enabledInStaging: Boolean,
    val enabledInProduction: Boolean
) {
    AUDIO_COMPRESSION(true, true, true),
    CLOUD_SYNC(true, true, true),
    PREMIUM_FEATURES(true, true, false),
    BETA_FEATURES(true, true, false),
    DEBUG_MENU(true, false, false),
    MOCK_API(true, false, false),
    ANALYTICS_LOGGING(false, true, true),
    CRASH_REPORTING(false, true, true)
}
