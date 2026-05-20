package com.gasparian.rob.feature.profile.presentation

import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.model.ProfileContact
import com.gasparian.rob.feature.profile.domain.model.ProfileLocation
import com.gasparian.rob.feature.profile.domain.model.ProfileQualification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ProfileUiMapperTest {
    @Test
    fun `maps successful profile result to content state`() {
        val state = Result.success(profile()).toProfileUiState()

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Robert Gasparyan", state.profile?.displayName)
        assertEquals("Toronto", state.profile?.location?.city)
        assertEquals("Core Expertise", state.profile?.summaryOfQualifications?.single()?.title)
    }

    @Test
    fun `maps failed profile result to error state`() {
        val state = Result.failure<Profile>(IllegalStateException("Profile failed")).toProfileUiState()

        assertFalse(state.isLoading)
        assertNull(state.profile)
        assertEquals("Profile failed", state.errorMessage)
    }

    private fun profile() = Profile(
        id = "profile",
        displayName = "Robert Gasparyan",
        headline = "Android Engineer",
        shortBio = "Short bio",
        location =
        ProfileLocation(
            city = "Toronto",
            region = "ON",
            country = "Canada",
            addressLine = null,
        ),
        contact =
        ProfileContact(
            email = "rob.gasparian@gmail.com",
            phone = "+1 437-551-9859",
            linkedin = "linkedin.com/in/rob-gasparian/",
        ),
        professionalProfile = "Professional profile",
        summaryOfQualifications =
        listOf(
            ProfileQualification(
                title = "Core Expertise",
                description = "Android development",
            ),
        ),
    )
}
