package com.gasparian.rob.core.network

sealed interface RcvNetworkResult<out T> {
    data class Success<T>(
        val data: T,
    ) : RcvNetworkResult<T>

    data class Failure(
        val error: RcvNetworkError,
    ) : RcvNetworkResult<Nothing>
}

sealed interface RcvNetworkError {
    data class Http(
        val code: Int,
        val message: String?,
    ) : RcvNetworkError

    data object NetworkUnavailable : RcvNetworkError

    data object Timeout : RcvNetworkError

    data object Serialization : RcvNetworkError

    data class Unknown(
        val message: String?,
    ) : RcvNetworkError
}
