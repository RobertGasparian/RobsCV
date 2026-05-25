package com.gasparian.rob.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeResult
import com.gasparian.rob.feature.home.domain.usecase.ClearHomeCacheUseCase
import com.gasparian.rob.feature.home.domain.usecase.GetHomeDataUseCase
import com.gasparian.rob.feature.home.domain.usecase.SyncHomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    getHomeDataUseCase: GetHomeDataUseCase,
    private val clearHomeCacheUseCase: ClearHomeCacheUseCase,
    private val syncHomeUseCase: SyncHomeUseCase,
) : ViewModel() {
    private val isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> =
        getHomeDataUseCase()
            .map(HomeResult<HomeData>::toHomeUiState)
            .combine(isRefreshing) { uiState, isRefreshing ->
                uiState.copy(isRefreshing = isRefreshing)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = HomeUiState.initialState(),
            )

    init {
        sync()
    }

    fun clearCache() {
        viewModelScope.launch {
            clearHomeCacheUseCase()
        }
    }

    fun refresh() {
        sync(showRefreshIndicator = true)
    }

    private fun sync(showRefreshIndicator: Boolean = false) {
        viewModelScope.launch {
            if (showRefreshIndicator) {
                isRefreshing.value = true
            }
            try {
                syncHomeUseCase()
            } finally {
                if (showRefreshIndicator) {
                    isRefreshing.value = false
                }
            }
        }
    }
}
