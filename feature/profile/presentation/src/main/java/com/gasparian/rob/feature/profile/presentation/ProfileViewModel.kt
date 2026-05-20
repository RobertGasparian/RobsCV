package com.gasparian.rob.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    getProfileUseCase: GetProfileUseCase,
) : ViewModel() {
    val uiState: StateFlow<ProfileUiState> =
        getProfileUseCase()
            .map(Result<Profile>::toProfileUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = ProfileUiState.initialState(),
            )
}
