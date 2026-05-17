package com.gasparian.rob.feature.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RcvHomeEducationResponseDto(
    val institutions: List<RcvHomeInstitutionDto>,
    val items: List<RcvHomeEducationItemDto>,
)

@Serializable
data class RcvHomeInstitutionDto(
    val id: String,
    val name: String,
    val shortName: String? = null,
    val type: String,
    val description: String,
    val websiteUrl: String,
    val location: RcvHomeLocationDto,
)

@Serializable
data class RcvHomeEducationItemDto(
    val id: String,
    val institutionId: String,
    val faculty: RcvHomeFacultyDto? = null,
    val program: RcvHomeEducationProgramDto,
    val startDate: String,
    val endDate: String,
    val status: String,
)

@Serializable
data class RcvHomeFacultyDto(
    val name: String,
)

@Serializable
data class RcvHomeEducationProgramDto(
    val name: String,
    val credential: String,
    val fieldOfStudy: String,
)
