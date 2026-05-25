package com.gasparian.rob.feature.profile.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gasparian.rob.feature.profile.presentation.ProfileViewModel
import com.gasparian.rob.feature.profile.ui.component.RcvProfileComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RcvProfileScreen(
    onOpenProfileSection: (sectionId: String) -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvProfileComponent(
        title = "Profile",
        uiState = uiState,
        buttonLabel = "Open profile detail",
        clearCacheLabel = "Clear profile cache",
        onButtonClick = { onOpenProfileSection("contact") },
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}

@Composable
fun RcvProfileDetailScreen(
    sectionId: String,
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvProfileComponent(
        title = "Profile detail: $sectionId",
        uiState = uiState,
        buttonLabel = "Back",
        clearCacheLabel = "Clear profile cache",
        onButtonClick = onBackClick,
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}
