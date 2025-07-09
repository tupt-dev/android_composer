package com.tupt.audio_composer.model

data class CoinMarketResponse<T>(
    val status: CoinMarketStatus?,
    val data: T,
    val error: String? = null
)