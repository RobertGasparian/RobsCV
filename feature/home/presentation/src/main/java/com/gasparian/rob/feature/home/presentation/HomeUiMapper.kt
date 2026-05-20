package com.gasparian.rob.feature.home.presentation

import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeError
import com.gasparian.rob.feature.home.domain.model.HomeResult

internal fun HomeResult<HomeData>.toHomeUiState(): HomeUiState = when (this) {
    is HomeResult.Success ->
        HomeUiState(
            isLoading = false,
            homeData = data,
            errorMessage = null,
        )

    is HomeResult.Failure ->
        HomeUiState(
            isLoading = false,
            homeData = null,
            errorMessage = error.toUiErrorMessage(),
        )
}

private fun HomeError.toUiErrorMessage(): String = when (this) {
    is HomeError.Http -> message ?: "Unable to load home data."
    HomeError.NetworkUnavailable -> "Network is unavailable."
    HomeError.Timeout -> "Loading timed out."
    HomeError.Serialization -> "Unable to read home data."
    is HomeError.Unknown -> message ?: "Unable to load home data."
}
