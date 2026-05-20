package com.gasparian.rob.feature.education.data.mapper

import com.gasparian.rob.feature.education.data.remote.EducationItemDto
import com.gasparian.rob.feature.education.data.remote.EducationLocationDto
import com.gasparian.rob.feature.education.data.remote.EducationProgramDto
import com.gasparian.rob.feature.education.data.remote.EducationResponseDto
import com.gasparian.rob.feature.education.data.remote.FacultyDto
import com.gasparian.rob.feature.education.data.remote.InstitutionDto
import com.gasparian.rob.feature.education.domain.model.EducationStatus
import com.gasparian.rob.feature.education.domain.model.InstitutionType
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RcvEducationMappersTest {
    @Test
    fun `toEntityGraph creates institutions locations and items`() {
        val graph = educationDto.toEntityGraph()

        assertEquals("ysu", graph.institutions.single().id)
        assertEquals("institution:ysu", graph.institutions.single().locationId)
        assertEquals("institution:ysu", graph.locations.single().id)
        assertEquals("Master's Degree", graph.items.single().credential)
    }

    @Test
    fun `toDomain joins institution with its location`() {
        val domain = educationDto.toEntityGraph().toDomain()

        assertEquals("Yerevan State University", domain.institutions.single().name)
        assertEquals(InstitutionType.PublicUniversity, domain.institutions.single().type)
        assertEquals("Yerevan", domain.institutions.single().location.city)
        assertEquals("Management", domain.items.single().program.fieldOfStudy)
        assertEquals(LocalDate.parse("2014-09-01"), domain.items.single().startDate)
        assertEquals(EducationStatus.Completed, domain.items.single().status)
    }
}

private val educationDto = EducationResponseDto(
    institutions = listOf(
        InstitutionDto(
            id = "ysu",
            name = "Yerevan State University",
            shortName = "YSU",
            type = "university",
            description = "Public university in Armenia.",
            websiteUrl = "https://www.ysu.am",
            location = EducationLocationDto(city = "Yerevan", country = "Armenia"),
        ),
    ),
    items = listOf(
        EducationItemDto(
            id = "ysu-management-master",
            institutionId = "ysu",
            faculty = FacultyDto(name = "Management"),
            program = EducationProgramDto(
                name = "Management",
                credential = "Master's Degree",
                fieldOfStudy = "Management",
            ),
            startDate = "2014-09-01",
            endDate = "2016-06-30",
            status = "completed",
        ),
    ),
)
