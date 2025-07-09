package com.tupt.audio_composer.data

import android.content.Context
import com.tupt.audio_composer.model.Product
import com.tupt.audio_composer.model.SampleProducts
import com.tupt.audio_composer.network.ApiClient
import com.tupt.audio_composer.network.NetworkResult
import com.tupt.audio_composer.network.safeApiCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository that handles product data operations
 */
class ProductRepository(private val context: Context) {

    /**
     * Get all products from API
     */
    fun getAllProductsFromApi(): Flow<NetworkResult<List<Product>>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            ApiClient.apiServiceCaching(context).getProducts()
        }
        emit(result)
    }

    /**
     * Get a single product by ID from API
     */
    fun getProductByIdFromApi(productId: Int): Flow<NetworkResult<Product>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            ApiClient.apiServiceCaching(context).getProductById(productId)
        }
        emit(result)
    }

    /**
     * Create a new product via API
     */
    fun createProductViaApi(product: Product): Flow<NetworkResult<Product>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            ApiClient.apiServiceCaching(context).createProduct(product)
        }
        emit(result)
    }

    /**
     * Update an existing product via API
     */
    fun updateProductViaApi(productId: Int, product: Product): Flow<NetworkResult<Product>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            ApiClient.apiServiceCaching(context).updateProduct(productId, product)
        }
        emit(result)
    }

    /**
     * Delete a product via API
     */
    fun deleteProductViaApi(productId: Int): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            ApiClient.apiServiceCaching(context).deleteProduct(productId)
        }
        emit(result)
    }

    /**
     * Search products
     */
    fun searchProducts(query: String): Flow<NetworkResult<List<Product>>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            ApiClient.apiServiceCaching(context).searchProducts(query)
        }
        emit(result)
    }

    /**
     * Get all products as a Flow (local/sample data - keeping for backward compatibility)
     */
    fun getAllProducts(): Flow<List<Product>> = flow {
        // Simulate network delay
        delay(1000) // 1 second delay
        // In a real app, this would fetch from a database or network
        emit(SampleProducts.products)
    }

    /**
     * Get a single product by ID as a Flow (local/sample data - keeping for backward compatibility)
     */
    fun getProductById(id: String): Flow<Product?> = flow {
        // In a real app, this would fetch from a database or network
        emit(SampleProducts.getProductById(id))
    }
}
