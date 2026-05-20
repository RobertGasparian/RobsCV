package com.gasparian.rob.feature.education.domain.model

import kotlinx.datetime.LocalDate

data class Education(
    val institutions: List<Institution>,
    val items: List<EducationItem>,
)

data class Institution(
    val id: String,
    val name: String,
    val shortName: String?,
    val type: InstitutionType,
    val description: String,
    val websiteUrl: String,
    val location: EducationLocation,
)

data class EducationLocation(
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

data class EducationItem(
    val id: String,
    val institutionId: String,
    val faculty: Faculty?,
    val program: EducationProgram,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: EducationStatus,
)

data class Faculty(
    val name: String,
)

data class EducationProgram(
    val name: String,
    val credential: String,
    val fieldOfStudy: String,
)

sealed interface InstitutionType {
    data object PublicUniversity : InstitutionType

    data object TrainingCenter : InstitutionType

    data class Unknown(
        val rawValue: String,
    ) : InstitutionType
}

sealed interface EducationStatus {
    data object Completed : EducationStatus

    data object InProgress : EducationStatus

    data class Unknown(
        val rawValue: String,
    ) : EducationStatus
}
