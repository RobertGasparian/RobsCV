package com.gasparian.rob.feature.skills.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.skills.domain.model.Skills
import com.gasparian.rob.feature.skills.domain.usecase.GetSkillsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SkillsViewModel(
    getSkillsUseCase: GetSkillsUseCase,
) : ViewModel() {
    val uiState: StateFlow<SkillsUiState> =
        getSkillsUseCase()
            .map(Result<Skills>::toSkillsUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = SkillsUiState.initialState(),
            )
}
