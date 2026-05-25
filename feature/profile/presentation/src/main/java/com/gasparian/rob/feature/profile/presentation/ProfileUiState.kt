package com.gasparian.rob.feature.profile.presentation

data class ProfileUiState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val profile: ProfileUiModel?,
    val errorMessage: String?,
) {
    companion object {
        fun initialState() = ProfileUiState(
            isLoading = true,
            isRefreshing = false,
            profile = null,
            errorMessage = null,
        )

        fun preview() = ProfileUiState(
            isLoading = false,
            isRefreshing = false,
            profile =
            ProfileUiModel(
                id = "profile-robert-gasparyan",
                displayName = "Robert Gasparyan",
                headline = "Android Engineer",
                shortBio = "Senior Android Engineer focused on Kotlin, Compose, architecture, and AI-assisted workflows.",
                location =
                ProfileLocationUiModel(
                    city = "Toronto",
                    region = "ON",
                    country = "Canada",
                    addressLine = null,
                ),
                contact =
                ProfileContactUiModel(
                    email = "rob.gasparian@gmail.com",
                    phone = "+1 437-551-9859",
                    linkedin = "linkedin.com/in/rob-gasparian/",
                ),
                professionalProfile = "A Senior Android Engineer with 8 years of experience across e-commerce, telecom, and multimedia.",
                summaryOfQualifications =
                listOf(
                    ProfileQualificationUiModel(
                        title = "Core Expertise",
                        description = "Kotlin, Java, Jetpack Compose, and modern Android architecture.",
                    ),
                ),
            ),
            errorMessage = null,
        )
    }
}

data class ProfileUiModel(
    val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val location: ProfileLocationUiModel,
    val contact: ProfileContactUiModel,
    val professionalProfile: String,
    val summaryOfQualifications: List<ProfileQualificationUiModel>,
)

data class ProfileLocationUiModel(
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

data class ProfileContactUiModel(
    val email: String,
    val phone: String,
    val linkedin: String,
)

data class ProfileQualificationUiModel(
    val title: String,
    val description: String,
)
