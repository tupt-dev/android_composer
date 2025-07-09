package com.tupt.audio_composer.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tupt.audio_composer.config.AppConfig

object SecureStorage {

    private const val SECURE_PREFS_NAME = "audio_composer_secure_prefs"
    private const val REGULAR_PREFS_NAME = "audio_composer_prefs"

    // Keys for storing sensitive data
    private const val KEY_API_TOKEN = "api_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_DEVICE_ID = "device_id"
    private const val KEY_LAST_SYNC_TIME = "last_sync_time"

    // Initialize secure storage
    fun getSecurePreferences(context: Context): SharedPreferences {
        return if (AppConfig.isProduction()) {
            // Sử dụng encrypted preferences trong production
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            EncryptedSharedPreferences.create(
                SECURE_PREFS_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            // Sử dụng regular preferences trong development/staging
            context.getSharedPreferences(SECURE_PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    // Get regular preferences for non-sensitive data
    fun getRegularPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(REGULAR_PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Token management
    fun saveApiToken(context: Context, token: String) {
        getSecurePreferences(context).edit()
            .putString(KEY_API_TOKEN, token)
            .commit()

    }

    fun getApiToken(context: Context): String? {
        return getSecurePreferences(context).getString(KEY_API_TOKEN, null)
    }

    fun saveRefreshToken(context: Context, refreshToken: String) {
        getSecurePreferences(context).edit()
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .commit()
    }

    fun getRefreshToken(context: Context): String? {
        return getSecurePreferences(context).getString(KEY_REFRESH_TOKEN, null)
    }

    // User data management
    fun saveUserId(context: Context, userId: String) {
        getSecurePreferences(context).edit()
            .putString(KEY_USER_ID, userId)
            .commit()
    }

    fun getUserId(context: Context): String? {
        return getSecurePreferences(context).getString(KEY_USER_ID, null)
    }

    fun saveDeviceId(context: Context, deviceId: String) {
        getSecurePreferences(context).edit()
            .putString(KEY_DEVICE_ID, deviceId)
            .commit()
    }

    fun getDeviceId(context: Context): String? {
        return getSecurePreferences(context).getString(KEY_DEVICE_ID, null)
    }

    // Sync management
    fun saveLastSyncTime(context: Context, timestamp: Long) {
        getRegularPreferences(context).edit()
            .putLong(KEY_LAST_SYNC_TIME, timestamp)
            .commit()
    }

    fun getLastSyncTime(context: Context): Long {
        return getRegularPreferences(context).getLong(KEY_LAST_SYNC_TIME, 0L)
    }

    // Authentication status
    fun isUserAuthenticated(context: Context): Boolean {
        val token = getApiToken(context)
        val userId = getUserId(context)
        return !token.isNullOrEmpty() && !userId.isNullOrEmpty()
    }

    // Clear all user data (logout)
    fun clearUserData(context: Context) {
        getSecurePreferences(context).edit()
            .remove(KEY_API_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_USER_ID)
            .commit()
    }

    // Clear all data (for testing or reset)
    fun clearAllData(context: Context) {
        getSecurePreferences(context).edit().clear().commit()
        getRegularPreferences(context).edit().clear().commit()
    }

    // Environment-specific settings
    fun saveEnvironmentSetting(context: Context, key: String, value: String) {
        val prefs = if (AppConfig.isProduction()) {
            getSecurePreferences(context)
        } else {
            getRegularPreferences(context)
        }

        prefs.edit().putString("env_$key", value).apply()
    }

    fun getEnvironmentSetting(context: Context, key: String): String? {
        val prefs = if (AppConfig.isProduction()) {
            getSecurePreferences(context)
        } else {
            getRegularPreferences(context)
        }

        return prefs.getString("env_$key", null)
    }
}
