package com.tupt.audio_composer.network

import com.tupt.audio_composer.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<Product>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): Response<List<Product>>
}
