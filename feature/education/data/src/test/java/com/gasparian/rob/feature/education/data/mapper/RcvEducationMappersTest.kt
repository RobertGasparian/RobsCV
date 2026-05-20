package com.gasparian.rob.feature.education.data.mapper

import com.gasparian.rob.feature.education.data.remote.RcvEducationItemDto
import com.gasparian.rob.feature.education.data.remote.RcvEducationLocationDto
import com.gasparian.rob.feature.education.data.remote.RcvEducationProgramDto
import com.gasparian.rob.feature.education.data.remote.RcvEducationResponseDto
import com.gasparian.rob.feature.education.data.remote.RcvFacultyDto
import com.gasparian.rob.feature.education.data.remote.RcvInstitutionDto
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
        assertEquals("Yerevan", domain.institutions.single().location.city)
        assertEquals("Management", domain.items.single().program.fieldOfStudy)
    }
}

private val educationDto = RcvEducationResponseDto(
    institutions = listOf(
        RcvInstitutionDto(
            id = "ysu",
            name = "Yerevan State University",
            shortName = "YSU",
            type = "university",
            description = "Public university in Armenia.",
            websiteUrl = "https://www.ysu.am",
            location = RcvEducationLocationDto(city = "Yerevan", country = "Armenia"),
        ),
    ),
    items = listOf(
        RcvEducationItemDto(
            id = "ysu-management-master",
            institutionId = "ysu",
            faculty = RcvFacultyDto(name = "Management"),
            program = RcvEducationProgramDto(
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
