package com.gasparian.rob.feature.education.presentation

import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.model.EducationItem
import com.gasparian.rob.feature.education.domain.model.EducationLocation
import com.gasparian.rob.feature.education.domain.model.EducationProgram
import com.gasparian.rob.feature.education.domain.model.EducationStatus
import com.gasparian.rob.feature.education.domain.model.Faculty
import com.gasparian.rob.feature.education.domain.model.Institution
import com.gasparian.rob.feature.education.domain.model.InstitutionType

internal fun Result<Education>.toEducationUiState(): EducationUiState = fold(
    onSuccess = { education ->
        EducationUiState(
            isLoading = false,
            education = education.toUiModel(),
            errorMessage = null,
        )
    },
    onFailure = { throwable ->
        EducationUiState(
            isLoading = false,
            education = null,
            errorMessage = throwable.toUiErrorMessage(),
        )
    },
)

private fun Education.toUiModel() = EducationUiModel(
    institutions = institutions.map(Institution::toUiModel),
    items = items.map(EducationItem::toUiModel),
)

private fun Institution.toUiModel() = InstitutionUiModel(
    id = id,
    name = name,
    shortName = shortName,
    type = type.toUiModel(),
    description = description,
    websiteUrl = websiteUrl,
    location = location.toUiModel(),
)

private fun EducationLocation.toUiModel() = EducationLocationUiModel(
    city = city,
    region = region,
    country = country,
    addressLine = addressLine,
)

private fun EducationItem.toUiModel() = EducationItemUiModel(
    id = id,
    institutionId = institutionId,
    faculty = faculty?.toUiModel(),
    program = program.toUiModel(),
    startDate = startDate,
    endDate = endDate,
    status = status.toUiModel(),
)

private fun Faculty.toUiModel() = FacultyUiModel(name = name)

private fun EducationProgram.toUiModel() = EducationProgramUiModel(
    name = name,
    credential = credential,
    fieldOfStudy = fieldOfStudy,
)

private fun InstitutionType.toUiModel() = when (this) {
    InstitutionType.PublicUniversity -> InstitutionTypeUiModel.PublicUniversity
    InstitutionType.TrainingCenter -> InstitutionTypeUiModel.TrainingCenter
    is InstitutionType.Unknown -> InstitutionTypeUiModel.Unknown(rawValue = rawValue)
}

private fun EducationStatus.toUiModel() = when (this) {
    EducationStatus.Completed -> EducationStatusUiModel.Completed
    EducationStatus.InProgress -> EducationStatusUiModel.InProgress
    is EducationStatus.Unknown -> EducationStatusUiModel.Unknown(rawValue = rawValue)
}

private fun Throwable.toUiErrorMessage(): String = message ?: "Unable to load education."
