package com.gasparian.rob.feature.experience.presentation

import kotlinx.datetime.LocalDate

data class ExperienceUiState(
    val isLoading: Boolean,
    val experience: ExperienceUiModel?,
    val errorMessage: String?,
) {
    companion object {
        fun initialState() = ExperienceUiState(
            isLoading = true,
            experience = null,
            errorMessage = null,
        )

        fun preview() = ExperienceUiState(
            isLoading = false,
            experience =
            ExperienceUiModel(
                roles =
                listOf(
                    ExperienceRoleUiModel(
                        title = "Senior Android Developer",
                        company = "DataArt",
                        startDate = LocalDate(2024, 5, 1),
                        endDate = null,
                        location = "Yerevan, Armenia",
                        workArrangement = WorkArrangementUiModel.Hybrid,
                        summary = "Contributed to a major online retailer Android app.",
                        highlights = listOf("Improved modularization and maintained CI/CD pipelines."),
                    ),
                ),
            ),
            errorMessage = null,
        )
    }
}

data class ExperienceUiModel(
    val roles: List<ExperienceRoleUiModel>,
)

data class ExperienceRoleUiModel(
    val title: String,
    val company: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val location: String,
    val workArrangement: WorkArrangementUiModel,
    val summary: String,
    val highlights: List<String>,
)

sealed interface WorkArrangementUiModel {
    data object Remote : WorkArrangementUiModel

    data object OnSite : WorkArrangementUiModel

    data object Hybrid : WorkArrangementUiModel

    data class Unknown(
        val rawValue: String,
    ) : WorkArrangementUiModel
}
