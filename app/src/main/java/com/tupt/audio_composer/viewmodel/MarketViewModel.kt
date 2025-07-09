package com.tupt.audio_composer.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tupt.audio_composer.data.CoinMarketRepository
import com.tupt.audio_composer.model.CryptoCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarketViewModel(context: Context) : ViewModel() {
    private val coinMarketRepository = CoinMarketRepository(context)

    private val _uiState: MutableStateFlow<MarketUiState> = MutableStateFlow(MarketUiState.Loading)
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    private val _cryptoCurrencies: MutableStateFlow<List<CryptoCurrency>> = MutableStateFlow(emptyList())
    val cryptoCurrencies: StateFlow<List<CryptoCurrency>> = _cryptoCurrencies.asStateFlow()

    fun fetchCryptoCurrencies() {
        // Simulate a network call to fetch crypto currencies
        viewModelScope.launch {
            try {
                _uiState.value = MarketUiState.Loading
                val currencies = coinMarketRepository.getCoinMarketData()
                _cryptoCurrencies.value = currencies
                _uiState.value = MarketUiState.Success(currencies)
            } catch (e: Exception) {
                Log.e("MarketViewModel", "Error fetching crypto currencies", e)
                _uiState.value = MarketUiState.Error("Failed to load crypto currencies: ${e.message}")
            }
        }
    }
}

sealed class MarketUiState {
    object Loading : MarketUiState()
    data class Success(val currencies: List<CryptoCurrency>) : MarketUiState()
    data class Error(val message: String) : MarketUiState()
}
