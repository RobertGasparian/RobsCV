package com.gasparian.rob.feature.home.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gasparian.rob.feature.home.presentation.HomeViewModel
import com.gasparian.rob.feature.home.ui.component.RcvHomeComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RcvHomeScreen(
    onOpenHomeDetail: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvHomeComponent(
        title = "Home",
        uiState = uiState,
        buttonLabel = "Open home detail",
        clearCacheLabel = "Clear whole DB cache",
        onButtonClick = onOpenHomeDetail,
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}

@Composable
fun RcvHomeDetailScreen(
    onBackClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvHomeComponent(
        title = "Home detail",
        uiState = uiState,
        buttonLabel = "Back",
        clearCacheLabel = "Clear whole DB cache",
        onButtonClick = onBackClick,
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}
