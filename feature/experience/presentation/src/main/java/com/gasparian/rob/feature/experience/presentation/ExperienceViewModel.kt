package com.gasparian.rob.feature.experience.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.usecase.ClearExperienceCacheUseCase
import com.gasparian.rob.feature.experience.domain.usecase.GetExperienceUseCase
import com.gasparian.rob.feature.experience.domain.usecase.SyncExperienceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExperienceViewModel(
    getExperienceUseCase: GetExperienceUseCase,
    private val clearExperienceCacheUseCase: ClearExperienceCacheUseCase,
    private val syncExperienceUseCase: SyncExperienceUseCase,
) : ViewModel() {
    private val isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<ExperienceUiState> =
        getExperienceUseCase()
            .map(Result<Experience>::toExperienceUiState)
            .combine(isRefreshing) { uiState, isRefreshing ->
                uiState.copy(isRefreshing = isRefreshing)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = ExperienceUiState.initialState(),
            )

    init {
        sync()
    }

    fun clearCache() {
        viewModelScope.launch {
            clearExperienceCacheUseCase()
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
                syncExperienceUseCase()
            } finally {
                if (showRefreshIndicator) {
                    isRefreshing.value = false
                }
            }
        }
    }
}
