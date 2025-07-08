package com.tupt.audio_composer.data

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
class ProductRepository {

    private val apiService = ApiClient.apiService

    /**
     * Get all products from API
     */
    fun getAllProductsFromApi(): Flow<NetworkResult<List<Product>>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            apiService.getProducts()
        }
        emit(result)
    }

    /**
     * Get a single product by ID from API
     */
    fun getProductByIdFromApi(id: Int): Flow<NetworkResult<Product>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            apiService.getProductById(id)
        }
        emit(result)
    }

    /**
     * Create a new product
     */
    fun createProduct(product: Product): Flow<NetworkResult<Product>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            apiService.createProduct(product)
        }
        emit(result)
    }

    /**
     * Update an existing product
     */
    fun updateProduct(id: Int, product: Product): Flow<NetworkResult<Product>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            apiService.updateProduct(id, product)
        }
        emit(result)
    }

    /**
     * Delete a product
     */
    fun deleteProduct(id: Int): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            apiService.deleteProduct(id)
        }
        emit(result)
    }

    /**
     * Search products
     */
    fun searchProducts(query: String): Flow<NetworkResult<List<Product>>> = flow {
        emit(NetworkResult.Loading())
        val result = safeApiCall {
            apiService.searchProducts(query)
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
