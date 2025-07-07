package com.tupt.audio_composer.data

import com.tupt.audio_composer.model.Product
import com.tupt.audio_composer.model.SampleProducts
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository that handles product data operations
 */
class ProductRepository {
    /**
     * Get all products as a Flow
     */
    fun getAllProducts(): Flow<List<Product>> = flow {
        // Simulate network delay
        delay(1000) // 1 second delay
        // In a real app, this would fetch from a database or network
        emit(SampleProducts.products)
    }

    /**
     * Get a single product by ID as a Flow
     */
    fun getProductById(id: String): Flow<Product?> = flow {
        // In a real app, this would fetch from a database or network
        emit(SampleProducts.getProductById(id))
    }
}
