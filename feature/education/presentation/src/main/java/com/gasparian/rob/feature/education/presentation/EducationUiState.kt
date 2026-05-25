package com.gasparian.rob.feature.education.presentation

import kotlinx.datetime.LocalDate

data class EducationUiState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val education: EducationUiModel?,
    val errorMessage: String?,
) {
    companion object {
        fun initialState() = EducationUiState(
            isLoading = true,
            isRefreshing = false,
            education = null,
            errorMessage = null,
        )

        fun preview() = EducationUiState(
            isLoading = false,
            isRefreshing = false,
            education =
            EducationUiModel(
                institutions =
                listOf(
                    InstitutionUiModel(
                        id = "ysu",
                        name = "Yerevan State University",
                        shortName = "YSU",
                        type = InstitutionTypeUiModel.PublicUniversity,
                        description = "A public university in Yerevan, Armenia.",
                        websiteUrl = "https://www.ysu.am",
                        location =
                        EducationLocationUiModel(
                            city = "Yerevan",
                            region = null,
                            country = "Armenia",
                            addressLine = null,
                        ),
                    ),
                ),
                items =
                listOf(
                    EducationItemUiModel(
                        id = "ysu-management-master",
                        institutionId = "ysu",
                        faculty = FacultyUiModel(name = "Management"),
                        program =
                        EducationProgramUiModel(
                            name = "Management",
                            credential = "Master's Degree",
                            fieldOfStudy = "Management",
                        ),
                        startDate = LocalDate(2014, 9, 1),
                        endDate = LocalDate(2016, 6, 30),
                        status = EducationStatusUiModel.Completed,
                    ),
                ),
            ),
            errorMessage = null,
        )
    }
}

data class EducationUiModel(
    val institutions: List<InstitutionUiModel>,
    val items: List<EducationItemUiModel>,
)

data class InstitutionUiModel(
    val id: String,
    val name: String,
    val shortName: String?,
    val type: InstitutionTypeUiModel,
    val description: String,
    val websiteUrl: String,
    val location: EducationLocationUiModel,
)

data class EducationLocationUiModel(
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

data class EducationItemUiModel(
    val id: String,
    val institutionId: String,
    val faculty: FacultyUiModel?,
    val program: EducationProgramUiModel,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: EducationStatusUiModel,
)

data class FacultyUiModel(
    val name: String,
)

data class EducationProgramUiModel(
    val name: String,
    val credential: String,
    val fieldOfStudy: String,
)

sealed interface InstitutionTypeUiModel {
    data object PublicUniversity : InstitutionTypeUiModel

    data object TrainingCenter : InstitutionTypeUiModel

    data class Unknown(
        val rawValue: String,
    ) : InstitutionTypeUiModel
}

sealed interface EducationStatusUiModel {
    data object Completed : EducationStatusUiModel

    data object InProgress : EducationStatusUiModel

    data class Unknown(
        val rawValue: String,
    ) : EducationStatusUiModel
}
