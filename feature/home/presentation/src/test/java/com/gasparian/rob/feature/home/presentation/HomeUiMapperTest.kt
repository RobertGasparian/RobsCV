package com.gasparian.rob.feature.home.presentation

import com.gasparian.rob.feature.home.domain.model.HomeData
import com.gasparian.rob.feature.home.domain.model.HomeError
import com.gasparian.rob.feature.home.domain.model.HomeResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class HomeUiMapperTest {
    @Test
    fun `maps successful home result to content state`() {
        val homeData = HomeData(
            profile = com.gasparian.rob.feature.profile.domain.model.Profile(
                id = "profile",
                displayName = "Robert Gasparyan",
                headline = "Android Engineer",
                shortBio = "Short bio",
                location = com.gasparian.rob.feature.profile.domain.model.ProfileLocation(
                    city = "Toronto",
                    region = "ON",
                    country = "Canada",
                    addressLine = null,
                ),
                contact = com.gasparian.rob.feature.profile.domain.model.ProfileContact(
                    email = "rob.gasparian@gmail.com",
                    phone = "+1 437-551-9859",
                    linkedin = "linkedin.com/in/rob-gasparian/",
                ),
                professionalProfile = "Professional profile",
                summaryOfQualifications = emptyList(),
            ),
            skills = com.gasparian.rob.feature.skills.domain.model.Skills(
                categories = emptyList(),
                skills = emptyList(),
            ),
            experience = com.gasparian.rob.feature.experience.domain.model.Experience(roles = emptyList()),
            education = com.gasparian.rob.feature.education.domain.model.Education(
                institutions = emptyList(),
                items = emptyList(),
            ),
            milestones = com.gasparian.rob.feature.milestones.domain.model.Milestones(
                currentFocus = com.gasparian.rob.feature.milestones.domain.model.CurrentFocus(
                    summary = "Focus",
                    topics = emptyList(),
                ),
                recentMilestones = emptyList(),
            ),
        )

        val state = HomeResult.Success(homeData).toHomeUiState()

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Robert Gasparyan", state.homeData?.profile?.displayName)
    }

    @Test
    fun `maps failed home result to error state`() {
        val state = HomeResult.Failure(HomeError.NetworkUnavailable).toHomeUiState()

        assertFalse(state.isLoading)
        assertNull(state.homeData)
        assertEquals("Network is unavailable.", state.errorMessage)
    }
}
