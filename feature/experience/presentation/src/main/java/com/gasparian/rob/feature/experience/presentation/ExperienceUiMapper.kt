package com.gasparian.rob.feature.experience.presentation

import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.model.ExperienceRole
import com.gasparian.rob.feature.experience.domain.model.WorkArrangement

internal fun Result<Experience>.toExperienceUiState(): ExperienceUiState = fold(
    onSuccess = { experience ->
        ExperienceUiState(
            isLoading = false,
            isRefreshing = false,
            experience = experience.toUiModel(),
            errorMessage = null,
        )
    },
    onFailure = { throwable ->
        ExperienceUiState(
            isLoading = false,
            isRefreshing = false,
            experience = null,
            errorMessage = throwable.toUiErrorMessage(),
        )
    },
)

private fun Experience.toUiModel() = ExperienceUiModel(
    roles = roles.map(ExperienceRole::toUiModel),
)

private fun ExperienceRole.toUiModel() = ExperienceRoleUiModel(
    title = title,
    company = company,
    startDate = startDate,
    endDate = endDate,
    location = location,
    workArrangement = workArrangement.toUiModel(),
    summary = summary,
    highlights = highlights,
)

private fun WorkArrangement.toUiModel() = when (this) {
    WorkArrangement.Remote -> WorkArrangementUiModel.Remote
    WorkArrangement.OnSite -> WorkArrangementUiModel.OnSite
    WorkArrangement.Hybrid -> WorkArrangementUiModel.Hybrid
    is WorkArrangement.Unknown -> WorkArrangementUiModel.Unknown(rawValue = rawValue)
}

private fun Throwable.toUiErrorMessage(): String = message ?: "Unable to load experience."
