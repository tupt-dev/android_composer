package com.tupt.audio_composer.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the settings screen
 */
class SettingsViewModel(private val context: Context) : ViewModel() {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
        private val HIGH_QUALITY_KEY = booleanPreferencesKey("high_quality")
    }

    // Dark mode state from DataStore
    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _highQualityPlayback = MutableStateFlow(false)
    val highQualityPlayback: StateFlow<Boolean> = _highQualityPlayback.asStateFlow()

    init {
        // Load settings from DataStore
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            context.dataStore.data.map { preferences ->
                preferences[DARK_MODE_KEY] ?: false
            }.collect { darkMode ->
                _darkMode.value = darkMode
            }
        }

        viewModelScope.launch {
            context.dataStore.data.map { preferences ->
                preferences[NOTIFICATIONS_KEY] ?: true
            }.collect { notifications ->
                _notificationsEnabled.value = notifications
            }
        }

        viewModelScope.launch {
            context.dataStore.data.map { preferences ->
                preferences[HIGH_QUALITY_KEY] ?: false
            }.collect { highQuality ->
                _highQualityPlayback.value = highQuality
            }
        }
    }

    /**
     * Toggle dark mode setting
     */
    fun toggleDarkMode() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                val currentValue = preferences[DARK_MODE_KEY] ?: false
                preferences[DARK_MODE_KEY] = !currentValue
            }
        }
    }

    /**
     * Toggle notifications setting
     */
    fun toggleNotifications() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                val currentValue = preferences[NOTIFICATIONS_KEY] ?: true
                preferences[NOTIFICATIONS_KEY] = !currentValue
            }
        }
    }

    /**
     * Toggle high quality playback setting
     */
    fun toggleHighQualityPlayback() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                val currentValue = preferences[HIGH_QUALITY_KEY] ?: false
                preferences[HIGH_QUALITY_KEY] = !currentValue
            }
        }
    }
}
