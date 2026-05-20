package com.gasparian.rob.feature.profile.domain.model

data class Profile(
    val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val location: ProfileLocation,
    val contact: ProfileContact,
    val professionalProfile: String,
    val summaryOfQualifications: List<ProfileQualification>,
)

data class ProfileLocation(
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

data class ProfileContact(
    val email: String,
    val phone: String,
    val linkedin: String,
)

data class ProfileQualification(
    val title: String,
    val description: String,
)
