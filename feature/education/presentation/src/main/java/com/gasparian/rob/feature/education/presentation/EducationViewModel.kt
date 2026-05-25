package com.gasparian.rob.feature.education.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.usecase.ClearEducationCacheUseCase
import com.gasparian.rob.feature.education.domain.usecase.GetEducationUseCase
import com.gasparian.rob.feature.education.domain.usecase.SyncEducationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EducationViewModel(
    getEducationUseCase: GetEducationUseCase,
    private val clearEducationCacheUseCase: ClearEducationCacheUseCase,
    private val syncEducationUseCase: SyncEducationUseCase,
) : ViewModel() {
    private val isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<EducationUiState> =
        getEducationUseCase()
            .map(Result<Education>::toEducationUiState)
            .combine(isRefreshing) { uiState, isRefreshing ->
                uiState.copy(isRefreshing = isRefreshing)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = EducationUiState.initialState(),
            )

    init {
        sync()
    }

    fun clearCache() {
        viewModelScope.launch {
            clearEducationCacheUseCase()
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
                syncEducationUseCase()
            } finally {
                if (showRefreshIndicator) {
                    isRefreshing.value = false
                }
            }
        }
    }
}
