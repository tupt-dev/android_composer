package com.tupt.audio_composer.util

import android.util.Log
import com.tupt.audio_composer.config.AppConfig

/**
 * Logging utility that respects environment configuration
 */
object LogUtils {
    private const val DEFAULT_TAG = "AudioComposer"

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (AppConfig.shouldEnableDebugLogging()) {
            Log.d(tag, message)
        }
    }

    fun i(message: String, tag: String = DEFAULT_TAG) {
        if (AppConfig.shouldEnableDebugLogging()) {
            Log.i(tag, message)
        }
    }

    fun w(message: String, tag: String = DEFAULT_TAG, throwable: Throwable? = null) {
        if (AppConfig.shouldEnableDebugLogging()) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }

    fun e(message: String, tag: String = DEFAULT_TAG, throwable: Throwable? = null) {
        // Error logs are always shown in all environments
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }

    fun network(message: String, tag: String = "Network") {
        if (AppConfig.shouldEnableNetworkLogging()) {
            Log.d(tag, message)
        }
    }

    fun printEnvironmentInfo() {
        if (AppConfig.shouldEnableDebugLogging()) {
            Log.d(DEFAULT_TAG, "=== Environment Info ===")
            Log.d(DEFAULT_TAG, "Environment: ${AppConfig.ENVIRONMENT}")
            Log.d(DEFAULT_TAG, "Base URL: ${AppConfig.BASE_URL}")
            Log.d(DEFAULT_TAG, "Debug Mode: ${AppConfig.isDebugMode()}")
            Log.d(DEFAULT_TAG, "Network Logging: ${AppConfig.shouldEnableNetworkLogging()}")
            Log.d(DEFAULT_TAG, "========================")
        }
    }
}
