package com.example.mypokedex.util

sealed class NetworkViewState<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): NetworkViewState<T>(data)
    class Error<T>(data: T? = null, message: String?): NetworkViewState<T>(data, message)
    class Loading<T>(data: T? = null): NetworkViewState<T>(data)
}