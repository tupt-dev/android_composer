package com.tupt.audio_composer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tupt.audio_composer.model.CryptoCurrency
import com.tupt.audio_composer.navigation.AppRoute
import com.tupt.audio_composer.viewmodel.HomeViewModel
import com.tupt.audio_composer.viewmodel.MarketUiState
import com.tupt.audio_composer.viewmodel.MarketViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val viewModel: MarketViewModel = viewModel(
        factory = remember {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MarketViewModel(context) as T
                }
            }
        }
    )
    val uiState by viewModel.uiState.collectAsState()
    val title: String = AppRoute.getTitle(navController.currentBackStackEntry)

    LaunchedEffect(viewModel) {
        viewModel.fetchCryptoCurrencies()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = uiState) {
                is MarketUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is MarketUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = currentState.currencies,
                            key = { coin: CryptoCurrency -> coin.id ?: coin.symbol ?: coin.name ?: "" }
                        ) { coin: CryptoCurrency ->
                            CryptoCurrencyCard(coin = coin)
                        }
                    }
                }
                is MarketUiState.Error -> {
                    Text(
                        text = currentState.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoCurrencyCard(coin: CryptoCurrency) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = coin.name ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = coin.symbol ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    coin.quote?.USD?.let { usd ->
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", usd.price ?: 0.0)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${String.format(Locale.US, "%.2f", usd.percent_change_24h ?: 0.0)}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if ((usd.percent_change_24h ?: 0.0) >= 0) Color.Green else Color.Red
                        )
                    }
                }
            }
        }
    }
}
