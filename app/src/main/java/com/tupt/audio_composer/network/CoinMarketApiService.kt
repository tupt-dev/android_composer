package com.tupt.audio_composer.network

import com.tupt.audio_composer.model.CoinMarketResponse
import com.tupt.audio_composer.model.CryptoCurrency
import retrofit2.Response
import retrofit2.http.GET

interface CoinMarketApiService {
    @GET(ApiConfig.CoinMarketEndpoints.COIN_LIST)
    suspend fun getCoinList(): Response<CoinMarketResponse<List<CryptoCurrency>>>
}