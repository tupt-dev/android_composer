package com.tupt.audio_composer.network

sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String, val code: Int? = null) : NetworkResult<T>()
    data class Loading<T>(val isLoading: Boolean = true) : NetworkResult<T>()
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> retrofit2.Response<T>
): NetworkResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                NetworkResult.Success(body)
            } ?: NetworkResult.Error("Response body is null")
        } else {
            NetworkResult.Error(
                message = response.message() ?: "Unknown error",
                code = response.code()
            )
        }
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message ?: "Network error occurred"
        )
    }
}
