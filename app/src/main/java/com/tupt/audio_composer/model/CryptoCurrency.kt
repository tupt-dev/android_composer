package com.tupt.audio_composer.model

data class CryptoCurrency(
    val id: Int?,
    val name: String?,
    val symbol: String?,
    val slug: String?,
    val num_market_pairs: Int?,
    val date_added: String?,
    val tags: List<String>?,
    val max_supply: String?,  // Changed from Long? to String? to handle large numbers
    val circulating_supply: Double?,
    val total_supply: Double?,
    val infinite_supply: Boolean?,
    val platform: Platform?,
    val cmc_rank: Int?,
    val self_reported_circulating_supply: Double?,
    val self_reported_market_cap: Double?,
    val tvl_ratio: Double?,
    val last_updated: String?,
    val quote: Quote?
)

data class Platform(
    val id: Int?,
    val name: String?,
    val symbol: String?,
    val slug: String?,
    val token_address: String?
)

data class Quote(
    val USD: USDQuote?
)

data class USDQuote(
    val price: Double?,
    val volume_24h: Double?,
    val volume_change_24h: Double?,
    val percent_change_1h: Double?,
    val percent_change_24h: Double?,
    val percent_change_7d: Double?,
    val percent_change_30d: Double?,
    val percent_change_60d: Double?,
    val percent_change_90d: Double?,
    val market_cap: Double?,
    val market_cap_dominance: Double?,
    val fully_diluted_market_cap: Double?,
    val tvl: Double?,
    val last_updated: String?
)
