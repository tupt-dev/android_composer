package com.tupt.audio_composer.model

data class CoinMarketStatus(
    val timestamp: String,
    val errorCode: Int,
    val errorMessage: String? = null,
    val elapsed: Int,
    val creditCount: Int,
    val notice: String? = null
)