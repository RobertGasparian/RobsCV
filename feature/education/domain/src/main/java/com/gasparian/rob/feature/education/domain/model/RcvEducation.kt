package com.gasparian.rob.feature.education.domain.model

data class RcvEducation(
    val institutions: List<RcvInstitution>,
    val items: List<RcvEducationItem>,
)

data class RcvInstitution(
    val id: String,
    val name: String,
    val shortName: String?,
    val type: String,
    val description: String,
    val websiteUrl: String,
    val location: RcvEducationLocation,
)

data class RcvEducationLocation(
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

data class RcvEducationItem(
    val id: String,
    val institutionId: String,
    val faculty: RcvFaculty?,
    val program: RcvEducationProgram,
    val startDate: String,
    val endDate: String,
    val status: String,
)

data class RcvFaculty(
    val name: String,
)

data class RcvEducationProgram(
    val name: String,
    val credential: String,
    val fieldOfStudy: String,
)
