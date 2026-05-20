package com.gasparian.rob.feature.home.domain.model

sealed interface HomeResult<out T> {
    data class Success<T>(
        val data: T,
    ) : HomeResult<T>

    data class Failure(
        val error: HomeError,
    ) : HomeResult<Nothing>
}

sealed interface HomeError {
    data class Http(
        val code: Int,
        val message: String?,
    ) : HomeError

    data object NetworkUnavailable : HomeError

    data object Timeout : HomeError

    data object Serialization : HomeError

    data class Unknown(
        val message: String?,
    ) : HomeError
}
