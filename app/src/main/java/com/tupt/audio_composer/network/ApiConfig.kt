package com.tupt.audio_composer.network

import com.tupt.audio_composer.BuildConfig

/**
 * Environment configuration for API endpoints and security settings
 */
object ApiConfig {

    /**
     * Base URL from BuildConfig based on product flavor
     */
    const val BASE_URL: String = BuildConfig.BASE_URL

    /**
     * Base URL for CoinMarketCap API
     */
    const val COIN_MARKET_BASE_URL: String = BuildConfig.COIN_MARKET_BASE_URL

    /**
     * API Key from BuildConfig (should be stored securely in production)
     */
    const val API_KEY: String = BuildConfig.API_KEY

    /**
     * Enable logging based on flavor
     */
    const val ENABLE_LOGGING: Boolean = BuildConfig.ENABLE_LOGGING

    /**
     * Environment name from BuildConfig
     */
    const val ENVIRONMENT: String = BuildConfig.ENVIRONMENT

    /**
     * Connection timeout in seconds
     */
    const val CONNECTION_TIMEOUT = 30L

    /**
     * Read timeout in seconds
     */
    const val READ_TIMEOUT = 30L

    /**
     * Write timeout in seconds
     */
    const val WRITE_TIMEOUT = 30L

    /**
     * Check if current build is debug
     */
    val isDebug: Boolean = BuildConfig.DEBUG

    /**
     * Check if current environment is development
     */
    const val isDevelopment: Boolean = ENVIRONMENT == "DEVELOPMENT"

    /**
     * Check if current environment is staging
     */
    const val isStaging: Boolean = ENVIRONMENT == "STAGING"

    /**
     * Check if current environment is production
     */
    const val isProduction: Boolean = ENVIRONMENT == "PRODUCTION"
}
