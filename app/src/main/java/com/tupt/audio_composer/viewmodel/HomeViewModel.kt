package com.tupt.audio_composer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tupt.audio_composer.data.ProductRepository
import com.tupt.audio_composer.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the home screen that manages the list of products
 */
class HomeViewModel : ViewModel() {
    private val TAG: String = this::class.java.simpleName
    private val repository = ProductRepository()

    // UI state
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Separate state for pull-to-refresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadProducts()
    }

    /**
     * Load all products from the repository
     */
    private fun loadProducts() {
        viewModelScope.launch {
            try {
                repository.getAllProducts().collect { products ->
                    Log.d(TAG, "loadProducts: $products")
                    _uiState.value = HomeUiState.Success(products)
                    Log.d(TAG, "Setting isRefreshing to false")
                    _isRefreshing.value = false // Stop refreshing when data is loaded
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading products", e)
                _uiState.value = HomeUiState.Error("Failed to load products: ${e.message}")
                Log.d(TAG, "Setting isRefreshing to false (error case)")
                _isRefreshing.value = false // Stop refreshing on error
            }
        }
    }

    /**
     * Refresh the products list
     */
    fun refreshProducts() {
        Log.d(TAG, "refreshProducts called - Setting isRefreshing to true")
        _isRefreshing.value = true
        loadProducts()
    }
}

/**
 * Represents the UI state for the home screen
 */
sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val products: List<Product>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
