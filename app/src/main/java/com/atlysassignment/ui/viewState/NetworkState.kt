package com.atlysassignment.ui.viewState

sealed class NetworkState<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>: NetworkState<T>()
    class Success<T>(data: T): NetworkState<T>(data, null)
    class Error<T>(message: String, data: T? = null): NetworkState<T>(null, message)
}