package com.tupt.audio_composer.network

import com.tupt.audio_composer.BuildConfig
import com.tupt.audio_composer.config.AppConfig

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
    val isDevelopment: Boolean = ENVIRONMENT == "DEVELOPMENT"

    /**
     * Check if current environment is staging
     */
    val isStaging: Boolean = ENVIRONMENT == "STAGING"

    /**
     * Check if current environment is production
     */
    val isProduction: Boolean = ENVIRONMENT == "PRODUCTION"

    object Endpoints {
        const val AUDIO_UPLOAD = "audio/upload"
        const val AUDIO_DOWNLOAD = "audio/download"
        const val AUDIO_LIST = "audio/list"
        const val USER_PROFILE = "user/profile"
        const val COMPOSITIONS = "compositions"
        const val RECORDINGS = "recordings"
        const val HEALTH_CHECK = "health"
        const val AUTH_LOGIN = "auth/login"
        const val AUTH_REGISTER = "auth/register"
        const val AUTH_REFRESH = "auth/refresh"
    }

    object CoinMarketEndpoints {
        const val COIN_LIST = "v1/cryptocurrency/listings/latest"
        const val COIN_DETAILS = "v1/cryptocurrency/info"
        const val COIN_PRICE = "v1/cryptocurrency/quotes/latest"
    }

    // Headers
    object Headers {
        const val AUTHORIZATION = "Authorization"
        const val CONTENT_TYPE = "Content-Type"
        const val ACCEPT = "Accept"
        const val USER_AGENT = "User-Agent"
        const val X_API_KEY = "X-API-Key"
        const val X_REQUESTED_WITH = "X-Requested-With"
        const val X_API_VERSION = "X-API-Version"
        const val X_ENVIRONMENT = "X-Environment"
    }

    // Content Types
    object ContentTypes {
        const val JSON = "application/json"
        const val MULTIPART = "multipart/form-data"
        const val AUDIO_MPEG = "audio/mpeg"
        const val AUDIO_WAV = "audio/wav"
        const val AUDIO_MP3 = "audio/mp3"
        const val AUDIO_OGG = "audio/ogg"
    }

    // Auth headers
    fun getAuthHeaders(token: String? = null): Map<String, String> = buildMap {
        if (!token.isNullOrEmpty()) {
            put(Headers.AUTHORIZATION, "Bearer $token")
        }
        put(Headers.X_API_KEY, AppConfig.API_KEY)
    }
}
