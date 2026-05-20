package com.gasparian.rob.feature.profile.presentation

import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.model.ProfileContact
import com.gasparian.rob.feature.profile.domain.model.ProfileLocation
import com.gasparian.rob.feature.profile.domain.model.ProfileQualification

internal fun Result<Profile>.toProfileUiState(): ProfileUiState = fold(
    onSuccess = { profile ->
        ProfileUiState(
            isLoading = false,
            profile = profile.toUiModel(),
            errorMessage = null,
        )
    },
    onFailure = { throwable ->
        ProfileUiState(
            isLoading = false,
            profile = null,
            errorMessage = throwable.toUiErrorMessage(),
        )
    },
)

private fun Profile.toUiModel() = ProfileUiModel(
    id = id,
    displayName = displayName,
    headline = headline,
    shortBio = shortBio,
    location = location.toUiModel(),
    contact = contact.toUiModel(),
    professionalProfile = professionalProfile,
    summaryOfQualifications = summaryOfQualifications.map(ProfileQualification::toUiModel),
)

private fun ProfileLocation.toUiModel() = ProfileLocationUiModel(
    city = city,
    region = region,
    country = country,
    addressLine = addressLine,
)

private fun ProfileContact.toUiModel() = ProfileContactUiModel(
    email = email,
    phone = phone,
    linkedin = linkedin,
)

private fun ProfileQualification.toUiModel() = ProfileQualificationUiModel(
    title = title,
    description = description,
)

private fun Throwable.toUiErrorMessage(): String = message ?: "Unable to load profile."
