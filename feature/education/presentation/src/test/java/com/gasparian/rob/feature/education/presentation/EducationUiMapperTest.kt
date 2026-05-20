package com.gasparian.rob.feature.education.presentation

import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.model.EducationItem
import com.gasparian.rob.feature.education.domain.model.EducationLocation
import com.gasparian.rob.feature.education.domain.model.EducationProgram
import com.gasparian.rob.feature.education.domain.model.EducationStatus
import com.gasparian.rob.feature.education.domain.model.Faculty
import com.gasparian.rob.feature.education.domain.model.Institution
import com.gasparian.rob.feature.education.domain.model.InstitutionType
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class EducationUiMapperTest {
    @Test
    fun `maps successful education result to content state`() {
        val state = Result.success(education()).toEducationUiState()

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Yerevan State University", state.education?.institutions?.single()?.name)
        assertEquals(EducationStatusUiModel.Completed, state.education?.items?.single()?.status)
    }

    @Test
    fun `maps failed education result to error state`() {
        val state = Result.failure<Education>(IllegalStateException("Education failed")).toEducationUiState()

        assertFalse(state.isLoading)
        assertNull(state.education)
        assertEquals("Education failed", state.errorMessage)
    }

    private fun education() = Education(
        institutions =
        listOf(
            Institution(
                id = "ysu",
                name = "Yerevan State University",
                shortName = "YSU",
                type = InstitutionType.PublicUniversity,
                description = "Public university.",
                websiteUrl = "https://www.ysu.am",
                location =
                EducationLocation(
                    city = "Yerevan",
                    region = null,
                    country = "Armenia",
                    addressLine = null,
                ),
            ),
        ),
        items =
        listOf(
            EducationItem(
                id = "master",
                institutionId = "ysu",
                faculty = Faculty(name = "Management"),
                program =
                EducationProgram(
                    name = "Management",
                    credential = "Master's Degree",
                    fieldOfStudy = "Management",
                ),
                startDate = LocalDate(2014, 9, 1),
                endDate = LocalDate(2016, 6, 30),
                status = EducationStatus.Completed,
            ),
        ),
    )
}
