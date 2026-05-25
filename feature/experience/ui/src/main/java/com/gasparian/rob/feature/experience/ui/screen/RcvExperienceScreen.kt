package com.gasparian.rob.feature.experience.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gasparian.rob.feature.experience.presentation.ExperienceViewModel
import com.gasparian.rob.feature.experience.ui.component.RcvExperienceComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RcvExperienceScreen(
    onOpenExperienceDetail: (experienceId: String) -> Unit,
    viewModel: ExperienceViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvExperienceComponent(
        title = "Experience",
        uiState = uiState,
        buttonLabel = "Open experience detail",
        clearCacheLabel = "Clear experience cache",
        onButtonClick = { onOpenExperienceDetail("priceline") },
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}

@Composable
fun RcvExperienceDetailScreen(
    experienceId: String,
    onBackClick: () -> Unit,
    viewModel: ExperienceViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvExperienceComponent(
        title = "Experience detail: $experienceId",
        uiState = uiState,
        buttonLabel = "Back",
        clearCacheLabel = "Clear experience cache",
        onButtonClick = onBackClick,
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}
