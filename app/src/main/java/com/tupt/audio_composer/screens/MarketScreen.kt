package com.tupt.audio_composer.screens

import androidx.compose.foundation.focusable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tupt.audio_composer.viewmodel.MarketUiState
import com.tupt.audio_composer.viewmodel.MarketViewModel
import java.util.Locale

@Composable
fun MarketScreen(
    navController: NavController,
    viewModel: MarketViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCryptoCurrencies()
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .focusable(),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = uiState) {
                MarketUiState.Loading -> {
                    Text(text = "Loading...")
                }

                is MarketUiState.Error -> {
                    Text(text = "Error: ${currentState.message}")
                }

                is MarketUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = currentState.data,
                            key = { coin -> coin.id ?: coin.symbol ?: coin.hashCode() }
                        ) { coin ->
                            CoinItem(
                                name = coin.name ?: "Unknown",
                                symbol = coin.symbol ?: "N/A",
                                price = coin.quote?.USD?.price ?: 0.0,
                                change = coin.quote?.USD?.percent_change_24h ?: 0.0
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoinItem(
    name: String,
    symbol: String,
    price: Double,
    change: Double
) {
    val formattedPrice = remember(price) {
        "$${String.format(Locale.US, "%.2f", price)}"
    }

    val formattedChange = remember(change) {
        "${if (change > 0) "+" else ""}${String.format(Locale.US, "%.2f", change)}%"
    }

    val changeColor = remember(change) {
        if (change > 0) Color.Green else Color.Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = formattedChange,
                    style = MaterialTheme.typography.bodyMedium,
                    color = changeColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
