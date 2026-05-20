package com.gasparian.rob.feature.experience.data.mapper

import com.gasparian.rob.feature.experience.data.remote.ExperienceResponseDto
import com.gasparian.rob.feature.experience.data.remote.ExperienceRoleDto
import com.gasparian.rob.feature.experience.domain.model.WorkArrangement
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RcvExperienceMappersTest {
    @Test
    fun `toEntityGraph creates stable role and highlight ids`() {
        val graph = experienceDto.toEntityGraph()

        assertEquals("priceline-android-engineer-2025-11-01", graph.roles.single().id)
        assertEquals("priceline-android-engineer-2025-11-01:highlight:0", graph.highlights.first().id)
        assertEquals(0, graph.highlights.first().sortIndex)
    }

    @Test
    fun `toDomain attaches sorted highlights to each role`() {
        val domain = experienceDto.toEntityGraph().toDomain()

        assertEquals("Priceline", domain.roles.single().company)
        assertEquals(LocalDate.parse("2025-11-01"), domain.roles.single().startDate)
        assertEquals(WorkArrangement.Hybrid, domain.roles.single().workArrangement)
        assertEquals(listOf("Built KMP foundation", "Introduced AI workflows"), domain.roles.single().highlights)
    }
}

private val experienceDto = ExperienceResponseDto(
    roles = listOf(
        ExperienceRoleDto(
            title = "Android Engineer",
            company = "Priceline",
            startDate = "2025-11-01",
            location = "Toronto, Canada",
            workArrangement = "hybrid",
            summary = "Travel technology platform.",
            highlights = listOf("Built KMP foundation", "Introduced AI workflows"),
        ),
    ),
)
