package com.gasparian.rob.feature.skills.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gasparian.rob.feature.skills.presentation.SkillsViewModel
import com.gasparian.rob.feature.skills.ui.component.RcvSkillsComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RcvSkillsScreen(
    onOpenSkillDetail: (skillId: String) -> Unit,
    viewModel: SkillsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvSkillsComponent(
        title = "Skills",
        uiState = uiState,
        buttonLabel = "Open skill detail",
        clearCacheLabel = "Clear skills cache",
        onButtonClick = { onOpenSkillDetail("compose") },
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}

@Composable
fun RcvSkillsDetailScreen(
    skillId: String,
    onBackClick: () -> Unit,
    viewModel: SkillsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RcvSkillsComponent(
        title = "Skill detail: $skillId",
        uiState = uiState,
        buttonLabel = "Back",
        clearCacheLabel = "Clear skills cache",
        onButtonClick = onBackClick,
        onClearCacheClick = viewModel::clearCache,
        onRefresh = viewModel::refresh,
    )
}
