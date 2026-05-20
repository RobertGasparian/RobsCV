package com.gasparian.rob.feature.milestones.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import com.gasparian.rob.feature.milestones.domain.usecase.GetMilestonesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MilestonesViewModel(
    getMilestonesUseCase: GetMilestonesUseCase,
) : ViewModel() {
    val uiState: StateFlow<MilestonesUiState> =
        getMilestonesUseCase()
            .map(Result<Milestones>::toMilestonesUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = MilestonesUiState.initialState(),
            )
}
