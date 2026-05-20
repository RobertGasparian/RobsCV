package com.gasparian.rob.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeResult
import com.gasparian.rob.feature.home.domain.usecase.GetHomeDataUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    getHomeDataUseCase: GetHomeDataUseCase,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> =
        getHomeDataUseCase()
            .map(HomeResult<HomeData>::toHomeUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = HomeUiState.initialState(),
            )
}
