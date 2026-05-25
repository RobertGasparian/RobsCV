package com.gasparian.rob.feature.milestones.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.milestones.domain.usecase.ClearMilestonesCacheUseCase
import com.gasparian.rob.feature.milestones.domain.usecase.GetMilestonesUseCase
import com.gasparian.rob.feature.milestones.domain.usecase.SyncMilestonesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MilestonesViewModel(
    getMilestonesUseCase: GetMilestonesUseCase,
    private val clearMilestonesCacheUseCase: ClearMilestonesCacheUseCase,
    private val syncMilestonesUseCase: SyncMilestonesUseCase,
) : ViewModel() {
    private val isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<MilestonesUiState> =
        getMilestonesUseCase()
            .map(Result<Milestones>::toMilestonesUiState)
            .combine(isRefreshing) { uiState, isRefreshing ->
                uiState.copy(isRefreshing = isRefreshing)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = MilestonesUiState.initialState(),
            )

    init {
        sync()
    }

    fun clearCache() {
        viewModelScope.launch {
            clearMilestonesCacheUseCase()
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
                syncMilestonesUseCase()
            } finally {
                if (showRefreshIndicator) {
                    isRefreshing.value = false
                }
            }
        }
    }
}
