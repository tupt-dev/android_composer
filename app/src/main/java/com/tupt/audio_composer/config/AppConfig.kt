package com.tupt.audio_composer.config

import com.tupt.audio_composer.BuildConfig

/**
 * Application configuration constants
 * Uses BuildConfig values set by Product Flavors
 */
object AppConfig {
    const val BASE_URL = BuildConfig.BASE_URL
    const val API_KEY = BuildConfig.API_KEY
    const val COINMARKET_BASE_URL = BuildConfig.COINMARKET_BASE_URL
    const val ENVIRONMENT = BuildConfig.ENVIRONMENT
    const val ENABLE_LOGGING = BuildConfig.ENABLE_LOGGING

    // Additional configuration constants
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val RETRY_COUNT = 3
    const val CACHE_SIZE_MB = 10L

    // Debug helpers
    fun isDebugMode(): Boolean = BuildConfig.DEBUG
    fun isDevelopment(): Boolean = ENVIRONMENT == "DEVELOPMENT"
    fun isStaging(): Boolean = ENVIRONMENT == "STAGING"
    fun isProduction(): Boolean = ENVIRONMENT == "PRODUCTION"

    // Logging configuration
    fun shouldEnableNetworkLogging(): Boolean = ENABLE_LOGGING && !isProduction()
    fun shouldEnableDebugLogging(): Boolean = ENABLE_LOGGING || isDebugMode()
}
