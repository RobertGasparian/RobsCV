package com.gasparian.rob.feature.education.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class EducationResponseDto(
    val institutions: List<InstitutionDto>,
    val items: List<EducationItemDto>,
)

@Serializable
data class InstitutionDto(
    val id: String,
    val name: String,
    val shortName: String? = null,
    val type: String,
    val description: String,
    val websiteUrl: String,
    val location: EducationLocationDto,
)

@Serializable
data class EducationLocationDto(
    val city: String,
    val region: String? = null,
    val country: String,
    val addressLine: String? = null,
)

@Serializable
data class EducationItemDto(
    val id: String,
    val institutionId: String,
    val faculty: FacultyDto? = null,
    val program: EducationProgramDto,
    val startDate: String,
    val endDate: String,
    val status: String,
)

@Serializable
data class FacultyDto(
    val name: String,
)

@Serializable
data class EducationProgramDto(
    val name: String,
    val credential: String,
    val fieldOfStudy: String,
)
