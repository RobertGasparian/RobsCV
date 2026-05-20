package com.gasparian.rob.feature.milestones.presentation

import com.gasparian.rob.feature.milestones.domain.model.CurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.Milestone
import com.gasparian.rob.feature.milestones.domain.model.Milestones

internal fun Result<Milestones>.toMilestonesUiState(): MilestonesUiState = fold(
    onSuccess = { milestones ->
        MilestonesUiState(
            isLoading = false,
            milestones = milestones.toUiModel(),
            errorMessage = null,
        )
    },
    onFailure = { throwable ->
        MilestonesUiState(
            isLoading = false,
            milestones = null,
            errorMessage = throwable.toUiErrorMessage(),
        )
    },
)

private fun Milestones.toUiModel() = MilestonesUiModel(
    currentFocus = currentFocus.toUiModel(),
    recentMilestones = recentMilestones.map(Milestone::toUiModel),
)

private fun CurrentFocus.toUiModel() = CurrentFocusUiModel(
    summary = summary,
    topics = topics,
)

private fun Milestone.toUiModel() = MilestoneUiModel(
    id = id,
    title = title,
    description = description,
    completedAt = completedAt,
    topics = topics,
)

private fun Throwable.toUiErrorMessage(): String = message ?: "Unable to load milestones."
