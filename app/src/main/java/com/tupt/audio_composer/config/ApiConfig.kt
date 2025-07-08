package com.tupt.audio_composer.config

import com.tupt.audio_composer.BuildConfig

object ApiConfig {

    // Base URLs từ BuildConfig
    const val BASE_URL = BuildConfig.BASE_URL
    const val COIN_MARKET_BASE_URL = BuildConfig.COINMARKET_BASE_URL

    // API Key bảo mật
    const val API_KEY = BuildConfig.API_KEY

    // Environment settings
    const val ENVIRONMENT = BuildConfig.ENVIRONMENT
    const val ENABLE_LOGGING = BuildConfig.ENABLE_LOGGING

    // Environment check properties
    val isDebug: Boolean = BuildConfig.DEBUG
    val isDevelopment: Boolean = ENVIRONMENT == "DEVELOPMENT"
    val isStaging: Boolean = ENVIRONMENT == "STAGING"
    val isProduction: Boolean = ENVIRONMENT == "PRODUCTION"

    // Timeout settings
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // API Endpoints
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

    // Security Configuration
    object Security {
        const val MIN_TLS_VERSION = "TLSv1.2"
        const val CERTIFICATE_PINNING_ENABLED = true
        const val OBFUSCATE_LOGS = true

        // Certificate pins (chỉ ví dụ - thay thế bằng pins thực tế)
        val CERTIFICATE_PINS = mapOf(
            "api.example.com" to listOf(
                "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
                "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB="
            )
        )
    }

    // Rate Limiting
    object RateLimit {
        val MAX_REQUESTS_PER_MINUTE = if (isProduction) 60 else 120
        const val MAX_CONCURRENT_REQUESTS = 10
        const val RETRY_DELAY_MS = 1000L
        const val MAX_RETRIES = 3
    }

    // Cache Configuration
    object Cache {
        const val CACHE_SIZE_MB = 50L
        const val CACHE_MAX_AGE_MINUTES = 5
        const val CACHE_MAX_STALE_HOURS = 24
    }

    // Utility functions
    fun isDebugMode(): Boolean = ENABLE_LOGGING && isDebug

    fun getEnvironmentName(): String = ENVIRONMENT

    fun getFullApiUrl(endpoint: String): String = "${BASE_URL}${endpoint}"

    fun getCoinMarketApiUrl(endpoint: String): String = "${COIN_MARKET_BASE_URL}${endpoint}"

    // Security headers
    fun getSecurityHeaders(): Map<String, String> = mapOf(
        Headers.CONTENT_TYPE to ContentTypes.JSON,
        Headers.ACCEPT to ContentTypes.JSON,
        Headers.USER_AGENT to "AudioComposer-Android/${BuildConfig.VERSION_NAME}",
        Headers.X_REQUESTED_WITH to "XMLHttpRequest",
        Headers.X_API_VERSION to "1.0",
        Headers.X_ENVIRONMENT to getEnvironmentName()
    )

    // Auth headers
    fun getAuthHeaders(token: String? = null): Map<String, String> = buildMap {
        putAll(getSecurityHeaders())
        if (!token.isNullOrEmpty()) {
            put(Headers.AUTHORIZATION, "Bearer $token")
        }
        put(Headers.X_API_KEY, API_KEY)
    }

    // Environment info for debugging
    fun getEnvironmentInfo(): String {
        return buildString {
            appendLine("=== ENVIRONMENT INFO ===")
            appendLine("Environment: $ENVIRONMENT")
            appendLine("Base URL: $BASE_URL")
            appendLine("CoinMarket URL: $COIN_MARKET_BASE_URL")
            appendLine("Debug Mode: $isDebug")
            appendLine("Logging Enabled: $ENABLE_LOGGING")
            appendLine("Is Development: $isDevelopment")
            appendLine("Is Staging: $isStaging")
            appendLine("Is Production: $isProduction")
            appendLine("App Version: ${BuildConfig.VERSION_NAME}")
            appendLine("Version Code: ${BuildConfig.VERSION_CODE}")
            appendLine("========================")
        }
    }
}
