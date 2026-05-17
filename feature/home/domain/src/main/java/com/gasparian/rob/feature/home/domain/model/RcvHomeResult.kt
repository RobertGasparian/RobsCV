package com.gasparian.rob.feature.home.domain.model

sealed interface RcvHomeResult<out T> {
    data class Success<T>(
        val data: T,
    ) : RcvHomeResult<T>

    data class Failure(
        val error: RcvHomeError,
    ) : RcvHomeResult<Nothing>
}

sealed interface RcvHomeError {
    data class Http(
        val code: Int,
        val message: String?,
    ) : RcvHomeError

    data object NetworkUnavailable : RcvHomeError

    data object Timeout : RcvHomeError

    data object Serialization : RcvHomeError

    data class Unknown(
        val message: String?,
    ) : RcvHomeError
}
