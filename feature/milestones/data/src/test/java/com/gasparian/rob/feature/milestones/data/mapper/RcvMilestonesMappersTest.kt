package com.gasparian.rob.feature.milestones.data.mapper

import com.gasparian.rob.feature.milestones.data.remote.CurrentFocusDto
import com.gasparian.rob.feature.milestones.data.remote.MilestoneDto
import com.gasparian.rob.feature.milestones.data.remote.MilestonesResponseDto
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RcvMilestonesMappersTest {
    @Test
    fun `toEntityGraph creates focus and milestone topics with ordering`() {
        val graph = milestonesDto.toEntityGraph(updatedAtMillis = 456L)

        assertEquals("KMP migration", graph.currentFocus.summary)
        assertEquals(456L, graph.currentFocus.updatedAtMillis)
        assertEquals(listOf(0, 1), graph.currentFocusTopics.map { it.sortIndex })
        assertEquals("nav3", graph.milestones.single().id)
        assertEquals("nav3", graph.milestoneTopics.single().milestoneId)
    }

    @Test
    fun `toDomain attaches topics to focus and recent milestones`() {
        val domain = milestonesDto.toEntityGraph(updatedAtMillis = 456L).toDomain()

        assertEquals(listOf("KMP", "Room"), domain.currentFocus.topics)
        assertEquals(LocalDate.parse("2026-05-18"), domain.recentMilestones.single().completedAt)
        assertEquals(listOf("Navigation 3"), domain.recentMilestones.single().topics)
    }
}

private val milestonesDto = MilestonesResponseDto(
    currentFocus = CurrentFocusDto(
        summary = "KMP migration",
        topics = listOf("KMP", "Room"),
    ),
    recentMilestones = listOf(
        MilestoneDto(
            id = "nav3",
            title = "Navigation skeleton",
            description = "Added Navigation 3 skeleton.",
            completedAt = "2026-05-18",
            topics = listOf("Navigation 3"),
        ),
    ),
)
