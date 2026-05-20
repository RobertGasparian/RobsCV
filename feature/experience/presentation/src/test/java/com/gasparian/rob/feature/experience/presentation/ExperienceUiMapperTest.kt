package com.gasparian.rob.feature.experience.presentation

import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.model.ExperienceRole
import com.gasparian.rob.feature.experience.domain.model.WorkArrangement
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ExperienceUiMapperTest {
    @Test
    fun `maps successful experience result to content state`() {
        val state = Result.success(experience()).toExperienceUiState()

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Priceline", state.experience?.roles?.single()?.company)
        assertEquals(WorkArrangementUiModel.Hybrid, state.experience?.roles?.single()?.workArrangement)
    }

    @Test
    fun `maps failed experience result to error state`() {
        val state = Result.failure<Experience>(IllegalStateException("Experience failed")).toExperienceUiState()

        assertFalse(state.isLoading)
        assertNull(state.experience)
        assertEquals("Experience failed", state.errorMessage)
    }

    private fun experience() = Experience(
        roles =
        listOf(
            ExperienceRole(
                title = "Android Developer",
                company = "Priceline",
                startDate = LocalDate(2025, 11, 1),
                endDate = null,
                location = "Toronto, Canada",
                workArrangement = WorkArrangement.Hybrid,
                summary = "Travel technology platform work.",
                highlights = listOf("KMP adoption"),
            ),
        ),
    )
}
