package com.tupt.audio_composer.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tupt.audio_composer.data.ProductRepository
import com.tupt.audio_composer.model.Product
import com.tupt.audio_composer.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the home screen that manages the list of products
 */
class HomeViewModel(private val context: Context) : ViewModel() {
    private val TAG: String = this::class.java.simpleName
    private val repository = ProductRepository(context)

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
     * Load all products from the repository (local data)
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
     * Load products from API
     */
    fun loadProductsFromApi() {
        viewModelScope.launch {
            repository.getAllProductsFromApi().collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.value = HomeUiState.Loading
                    }
                    is NetworkResult.Success -> {
                        Log.d(TAG, "API Success: ${result.data}")
                        _uiState.value = HomeUiState.Success(result.data)
                        _isRefreshing.value = false
                    }
                    is NetworkResult.Error -> {
                        Log.e(TAG, "API Error: ${result.message}")
                        _uiState.value = HomeUiState.Error(result.message)
                        _isRefreshing.value = false
                    }
                }
            }
        }
    }

    /**
     * Search products from API
     */
    fun searchProducts(query: String) {
        viewModelScope.launch {
            repository.searchProducts(query).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.value = HomeUiState.Loading
                    }
                    is NetworkResult.Success -> {
                        _uiState.value = HomeUiState.Success(result.data)
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = HomeUiState.Error(result.message)
                    }
                }
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
