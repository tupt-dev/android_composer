package com.tupt.audio_composer.screens

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tupt.audio_composer.viewmodel.SettingsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(context.applicationContext as Application) as T
            }
        }
    )

    // Collect the settings states from the ViewModel
    val darkMode by settingsViewModel.darkMode.collectAsStateWithLifecycle()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsStateWithLifecycle()
    val highQualityPlayback by settingsViewModel.highQualityPlayback.collectAsStateWithLifecycle()

    Scaffold(
    modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Settings options
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Dark Mode Setting
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Dark Mode")
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { settingsViewModel.toggleDarkMode() }
                        )
                    }

                    HorizontalDivider()

                    // Notifications Setting
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Enable Notifications")
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { settingsViewModel.toggleNotifications() }
                        )
                    }

                    HorizontalDivider()

                    // High Quality Playback Setting
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "High Quality Playback")
                        Switch(
                            checked = highQualityPlayback,
                            onCheckedChange = { settingsViewModel.toggleHighQualityPlayback() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun SettingsScreenPreview() {
    val context = LocalContext.current
    val viewModel = SettingsViewModel(
        application = context.applicationContext as Application
    ).apply {
        toggleDarkMode()
    }
    SettingsScreen(
        navController = NavController(context = context),
    )
}