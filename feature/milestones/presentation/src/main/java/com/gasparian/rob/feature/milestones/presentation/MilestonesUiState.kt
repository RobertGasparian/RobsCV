package com.gasparian.rob.feature.milestones.presentation

import kotlinx.datetime.LocalDate

data class MilestonesUiState(
    val isLoading: Boolean,
    val milestones: MilestonesUiModel?,
    val errorMessage: String?,
) {
    companion object {
        fun initialState() = MilestonesUiState(
            isLoading = true,
            milestones = null,
            errorMessage = null,
        )

        fun preview() = MilestonesUiState(
            isLoading = false,
            milestones =
            MilestonesUiModel(
                currentFocus =
                CurrentFocusUiModel(
                    summary = "Building a modern Android CV app with KMP-friendly architecture.",
                    topics = listOf("Navigation 3", "Room KMP", "Material 3 Expressive"),
                ),
                recentMilestones =
                listOf(
                    MilestoneUiModel(
                        id = "room-kmp",
                        title = "Room KMP",
                        description = "Added structured local persistence as the source of truth.",
                        completedAt = LocalDate(2026, 5, 1),
                        topics = listOf("Room", "KMP"),
                    ),
                ),
            ),
            errorMessage = null,
        )
    }
}

data class MilestonesUiModel(
    val currentFocus: CurrentFocusUiModel,
    val recentMilestones: List<MilestoneUiModel>,
)

data class CurrentFocusUiModel(
    val summary: String,
    val topics: List<String>,
)

data class MilestoneUiModel(
    val id: String,
    val title: String,
    val description: String,
    val completedAt: LocalDate,
    val topics: List<String>,
)
