package com.gasparian.rob.feature.education.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.usecase.GetEducationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EducationViewModel(
    getEducationUseCase: GetEducationUseCase,
) : ViewModel() {
    val uiState: StateFlow<EducationUiState> =
        getEducationUseCase()
            .map(Result<Education>::toEducationUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = EducationUiState.initialState(),
            )
}
