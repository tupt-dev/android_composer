package com.tupt.audio_composer.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.tupt.audio_composer.config.AppConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val CACHE_SIZE = 30 * 1024 * 1024 // 10 MB
    private var cache: okhttp3.Cache? = null

    private fun getCache(context: Context): okhttp3.Cache {
        if (cache == null) {
            cache = okhttp3.Cache(
                File(context.cacheDir, "http_cache"),
                CACHE_SIZE.toLong()
            )
        }
        return cache!!
    }

    private val gson = GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .create()

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
        if (chain.request().url.toString().startsWith(ApiConfig.COIN_MARKET_BASE_URL)) {
            requestBuilder.header("X-CMC_PRO_API_KEY", AppConfig.API_KEY)
        }
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (AppConfig.shouldEnableNetworkLogging()) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val cacheInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())

        if (chain.request().method == "GET") {
            response.newBuilder()
                .header("Cache-Control", "max-age=300") // 5 phút
                .removeHeader("Pragma")
                .removeHeader("Expires")
                .build()
        } else {
            response
        }
    }

    private val writeThroughCacheInterceptor = Interceptor { chain ->
        val request = chain.request()

        val newRequest = request.newBuilder()
            .cacheControl(CacheControl.Builder().noCache().build())
            .build()

        val response = chain.proceed(newRequest)

        if (request.method == "GET") {
            response.newBuilder()
                .header("Cache-Control", "max-age=300") // Cache 5 phút
                .removeHeader("Pragma")
                .removeHeader("Expires")
                .build()
        } else {
            response
        }
    }

    private val readThroughCacheInterceptor = Interceptor { chain ->
        val request = chain.request()

        val cacheControl = CacheControl.Builder()
            .maxAge(5, TimeUnit.MINUTES)
            .build()

        val newRequest = request.newBuilder()
            .cacheControl(cacheControl)
            .build()

        chain.proceed(newRequest)
    }

    private fun getWriteThroughOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(getCache(context))
            .addInterceptor(authInterceptor)
            .addInterceptor(writeThroughCacheInterceptor)
            .apply {
                if (AppConfig.shouldEnableNetworkLogging()) {
                    addInterceptor(loggingInterceptor)
                }
            }
            .connectTimeout(ApiConfig.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun getReadThroughOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(getCache(context))
            .addInterceptor(authInterceptor)
            .addInterceptor(readThroughCacheInterceptor)
            .addNetworkInterceptor(cacheInterceptor)
            .apply {
                if (AppConfig.shouldEnableNetworkLogging()) {
                    addInterceptor(loggingInterceptor)
                }
            }
            .connectTimeout(ApiConfig.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    fun apiServiceCaching(context: Context): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(getReadThroughOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun coinMarketApiServiceCaching(context: Context): CoinMarketApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.COIN_MARKET_BASE_URL)
            .client(getReadThroughOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(CoinMarketApiService::class.java)
    }

    fun coinMarketApiServiceWriteThrough(context: Context): CoinMarketApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.COIN_MARKET_BASE_URL)
            .client(getWriteThroughOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(CoinMarketApiService::class.java)
    }
}
