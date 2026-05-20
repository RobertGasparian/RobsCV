package com.gasparian.rob.feature.home.presentation

import com.gasparian.rob.feature.home.domain.model.HomeData

data class HomeUiState(
    val isLoading: Boolean,
    val homeData: HomeData?,
    val errorMessage: String?,
) {
    companion object {
        fun initialState() = HomeUiState(
            isLoading = true,
            homeData = null,
            errorMessage = null,
        )

        fun preview() = HomeUiState(
            isLoading = false,
            homeData = null,
            errorMessage = null,
        )
    }
}
