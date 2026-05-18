package com.gasparian.rob.feature.education.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RcvEducationResponseDto(
    val institutions: List<RcvInstitutionDto>,
    val items: List<RcvEducationItemDto>,
)

@Serializable
data class RcvInstitutionDto(
    val id: String,
    val name: String,
    val shortName: String? = null,
    val type: String,
    val description: String,
    val websiteUrl: String,
    val location: RcvEducationLocationDto,
)

@Serializable
data class RcvEducationLocationDto(
    val city: String,
    val region: String? = null,
    val country: String,
    val addressLine: String? = null,
)

@Serializable
data class RcvEducationItemDto(
    val id: String,
    val institutionId: String,
    val faculty: RcvFacultyDto? = null,
    val program: RcvEducationProgramDto,
    val startDate: String,
    val endDate: String,
    val status: String,
)

@Serializable
data class RcvFacultyDto(
    val name: String,
)

@Serializable
data class RcvEducationProgramDto(
    val name: String,
    val credential: String,
    val fieldOfStudy: String,
)
