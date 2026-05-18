package com.gasparian.rob.feature.profile.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RcvProfileResponseDto(
    val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val location: RcvProfileLocationDto,
    val contact: RcvProfileContactDto,
    val professionalProfile: String,
    val summaryOfQualifications: List<RcvProfileQualificationDto>,
)

@Serializable
data class RcvProfileLocationDto(
    val city: String,
    val region: String? = null,
    val country: String,
    val addressLine: String? = null,
)

@Serializable
data class RcvProfileContactDto(
    val email: String,
    val phone: String,
    val linkedin: String,
)

@Serializable
data class RcvProfileQualificationDto(
    val title: String,
    val description: String,
)
