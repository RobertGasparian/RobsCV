package com.gasparian.rob.feature.experience.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.usecase.GetExperienceUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ExperienceViewModel(
    getExperienceUseCase: GetExperienceUseCase,
) : ViewModel() {
    val uiState: StateFlow<ExperienceUiState> =
        getExperienceUseCase()
            .map(Result<Experience>::toExperienceUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = ExperienceUiState.initialState(),
            )
}
