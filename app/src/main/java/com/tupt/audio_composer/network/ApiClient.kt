package com.tupt.audio_composer.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    /**
     * Auth interceptor to add API key to requests
     */
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer ${ApiConfig.API_KEY}")
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    /**
     * Logging interceptor - only enabled in debug mode
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (ApiConfig.ENABLE_LOGGING) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .apply {
            if (ApiConfig.ENABLE_LOGGING) {
                addInterceptor(loggingInterceptor)
            }
        }
        .connectTimeout(ApiConfig.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val retrofitCoinMarket = Retrofit.Builder()
        .baseUrl(ApiConfig.COINMARKET_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    var coinMarketApiService: CoinMarketApiService = retrofitCoinMarket.create(CoinMarketApiService::class.java)

    /**
     * Get current environment info for debugging
     */
    fun getEnvironmentInfo(): String {
        return "Environment: ${ApiConfig.ENVIRONMENT}\n" +
               "Base URL: ${ApiConfig.BASE_URL}\n" +
               "Logging: ${ApiConfig.ENABLE_LOGGING}\n" +
               "Debug: ${ApiConfig.isDebug}\n" +
               "Is Development: ${ApiConfig.isDevelopment}\n" +
               "Is Staging: ${ApiConfig.isStaging}\n" +
               "Is Production: ${ApiConfig.isProduction}"
    }
}
