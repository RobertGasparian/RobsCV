package com.gasparian.rob.feature.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RcvHomeProfileResponseDto(
    val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val location: RcvHomeLocationDto,
    val contact: RcvHomeContactDto,
    val professionalProfile: String,
    val summaryOfQualifications: List<RcvHomeQualificationDto>,
)

@Serializable
data class RcvHomeLocationDto(
    val city: String,
    val region: String? = null,
    val country: String,
    val addressLine: String? = null,
)

@Serializable
data class RcvHomeContactDto(
    val email: String,
    val phone: String,
    val linkedin: String,
)

@Serializable
data class RcvHomeQualificationDto(
    val title: String,
    val description: String,
)
