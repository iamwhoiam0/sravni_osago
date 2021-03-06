package com.example.osagosravni.utils

sealed interface NetworkState<out T: Any> {
    data class Success<out T: Any>(val data : T) : NetworkState<T>
    object InvalidData :NetworkState<Nothing>
    data class Error(val error : String) : NetworkState<Nothing>
    data class NetworkException(val error : String?) : NetworkState<Nothing>
    sealed class HttpErrors : NetworkState<Nothing> {
        data class ResourceForbidden(val exception: String) : HttpErrors()
        data class ResourceNotFound(val exception: String) : HttpErrors()
        data class InternalServerError(val exception: String) : HttpErrors()
        data class BadGateWay(val exception: String) : HttpErrors()
        data class ResourceRemoved(val exception: String) : HttpErrors()
        data class RemovedResourceFound(val exception: String) : HttpErrors()
    }
}


