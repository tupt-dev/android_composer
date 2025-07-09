package com.tupt.audio_composer.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the settings screen
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val Application.dataStore by preferencesDataStore(name = "settings")
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
        // Load settings from DataStore when ViewModel is created
        viewModelScope.launch {
            getApplication<Application>().dataStore.data
                .map { preferences ->
                    preferences[DARK_MODE_KEY] ?: false
                }
                .collect { darkModeValue ->
                    _darkMode.value = darkModeValue
                }
        }

        viewModelScope.launch {
            getApplication<Application>().dataStore.data
                .map { preferences ->
                    preferences[NOTIFICATIONS_KEY] ?: true
                }
                .collect { notificationsValue ->
                    _notificationsEnabled.value = notificationsValue
                }
        }

        viewModelScope.launch {
            getApplication<Application>().dataStore.data
                .map { preferences ->
                    preferences[HIGH_QUALITY_KEY] ?: false
                }
                .collect { highQualityValue ->
                    _highQualityPlayback.value = highQualityValue
                }
        }
    }

    /**
     * Toggle dark mode setting
     */
    fun toggleDarkMode() {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { preferences ->
                preferences[DARK_MODE_KEY] = !_darkMode.value
            }
        }
    }

    /**
     * Toggle notifications setting
     */
    fun toggleNotifications() {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { preferences ->
                preferences[NOTIFICATIONS_KEY] = !_notificationsEnabled.value
            }
        }
    }

    /**
     * Toggle high quality playback setting
     */
    fun toggleHighQualityPlayback() {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { preferences ->
                preferences[HIGH_QUALITY_KEY] = !_highQualityPlayback.value
            }
        }
    }
}
