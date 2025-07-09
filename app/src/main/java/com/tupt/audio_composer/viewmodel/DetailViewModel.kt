package com.tupt.audio_composer.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tupt.audio_composer.data.ProductRepository
import com.tupt.audio_composer.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the detail screen that manages a single product
 */
class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val context: Context
) : ViewModel() {
    private val repository = ProductRepository(context)

    // Get the product ID from the saved state handle (navigation arguments)
    private val productId: String = checkNotNull(savedStateHandle["productId"])

    // UI state
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    /**
     * Load the product from the repository
     */
    private fun loadProduct() {
        viewModelScope.launch {
            repository.getProductById(productId).collect { product ->
                _uiState.value = if (product != null) {
                    DetailUiState.Success(product)
                } else {
                    DetailUiState.Error("Product not found")
                }
            }
        }
    }

    /**
     * Refresh the current product data
     */
    fun refreshProduct() {
        _uiState.value = DetailUiState.Loading
        loadProduct()
    }
}

/**
 * Represents the UI state for the detail screen
 */
sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Success(val product: Product) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
