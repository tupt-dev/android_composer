package com.tupt.audio_composer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.tupt.audio_composer.screens.MainScreen
import com.tupt.audio_composer.ui.theme.Audio_composerTheme
import com.tupt.audio_composer.viewmodel.SettingsViewModel
import com.tupt.audio_composer.environment.EnvironmentManager
import com.tupt.audio_composer.config.ApiConfig
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize environment
        EnvironmentManager.initializeEnvironment(this)

        setContent {
            val settingsViewModel = remember { SettingsViewModel(this@MainActivity) }
            val darkMode by settingsViewModel.darkMode.collectAsStateWithLifecycle()

            Audio_composerTheme(darkTheme = darkMode) {
                // Use MainScreen with bottom navigation
                MainScreen(settingsViewModel = settingsViewModel)
            }

            // Initialize environment in Compose - moved to LaunchedEffect
            LaunchedEffect(Unit) {
                if (ApiConfig.isDebugMode()) {
                    Log.d("MainActivity", ApiConfig.getEnvironmentInfo())
                }
            }
        }
    }
}

// Keep existing drawer content for compatibility
@Composable
fun DrawerContent(
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Audio Composer",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Environment info for debug
        if (ApiConfig.isDebugMode()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Environment: ${ApiConfig.ENVIRONMENT}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Debug Mode: ${ApiConfig.isDebugMode()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        DrawerItem(
            icon = Icons.Default.Home,
            text = "Home",
            onClick = onHomeClick
        )

        DrawerItem(
            icon = Icons.Default.Settings,
            text = "Settings",
            onClick = onSettingsClick
        )
    }
}

@Composable
fun DrawerItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}