package com.gasparian.rob.feature.profile.domain.model

data class RcvProfile(
    val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val location: RcvProfileLocation,
    val contact: RcvProfileContact,
    val professionalProfile: String,
    val summaryOfQualifications: List<RcvProfileQualification>,
)

data class RcvProfileLocation(
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

data class RcvProfileContact(
    val email: String,
    val phone: String,
    val linkedin: String,
)

data class RcvProfileQualification(
    val title: String,
    val description: String,
)
