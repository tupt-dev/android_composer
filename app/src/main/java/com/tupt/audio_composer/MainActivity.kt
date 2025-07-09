package com.tupt.audio_composer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tupt.audio_composer.screens.MainScreen
import com.tupt.audio_composer.ui.theme.Audio_composerTheme
import com.tupt.audio_composer.viewmodel.SettingsViewModel
import androidx.compose.ui.graphics.toArgb
import com.tupt.audio_composer.ui.theme.Black
import com.tupt.audio_composer.ui.theme.Grey40

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsViewModel = remember { SettingsViewModel(this@MainActivity) }
            val darkMode by settingsViewModel.darkMode.collectAsStateWithLifecycle()
            window.statusBarColor = if (darkMode) {
                Black.toArgb()
            } else {
                Grey40.toArgb()
            }
            Audio_composerTheme(darkTheme = darkMode) {
                // Use MainScreen with bottom navigation
                MainScreen(settingsViewModel = settingsViewModel)
            }
        }
    }
}
