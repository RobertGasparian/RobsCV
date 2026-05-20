package com.gasparian.rob.feature.profile.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val location: ProfileLocationDto,
    val contact: ProfileContactDto,
    val professionalProfile: String,
    val summaryOfQualifications: List<ProfileQualificationDto>,
)

@Serializable
data class ProfileLocationDto(
    val city: String,
    val region: String? = null,
    val country: String,
    val addressLine: String? = null,
)

@Serializable
data class ProfileContactDto(
    val email: String,
    val phone: String,
    val linkedin: String,
)

@Serializable
data class ProfileQualificationDto(
    val title: String,
    val description: String,
)
