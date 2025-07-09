package com.tupt.audio_composer.data

import android.content.Context
import com.tupt.audio_composer.model.CryptoCurrency
import com.tupt.audio_composer.network.ApiClient

class CoinMarketRepository(private val context: Context) {

    suspend fun getCoinMarketData(): List<CryptoCurrency> {
        val apiService = ApiClient.coinMarketApiServiceCaching(context)
        val response = apiService.getCoinList()
        if (response.isSuccessful) {
            return response.body().let { coinMarketResponse ->
                coinMarketResponse?.data?.map { it }
            } ?: emptyList()
        } else {
            throw Exception("Failed to fetch coin market data: ${response.errorBody()?.string()}")
        }
    }
}