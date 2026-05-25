package com.gasparian.rob.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.usecase.ClearProfileCacheUseCase
import com.gasparian.rob.feature.profile.domain.usecase.GetProfileUseCase
import com.gasparian.rob.feature.profile.domain.usecase.SyncProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    getProfileUseCase: GetProfileUseCase,
    private val clearProfileCacheUseCase: ClearProfileCacheUseCase,
    private val syncProfileUseCase: SyncProfileUseCase,
) : ViewModel() {
    private val isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<ProfileUiState> =
        getProfileUseCase()
            .map(Result<Profile>::toProfileUiState)
            .combine(isRefreshing) { uiState, isRefreshing ->
                uiState.copy(isRefreshing = isRefreshing)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = ProfileUiState.initialState(),
            )

    init {
        sync()
    }

    fun clearCache() {
        viewModelScope.launch {
            clearProfileCacheUseCase()
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
                syncProfileUseCase()
            } finally {
                if (showRefreshIndicator) {
                    isRefreshing.value = false
                }
            }
        }
    }
}
