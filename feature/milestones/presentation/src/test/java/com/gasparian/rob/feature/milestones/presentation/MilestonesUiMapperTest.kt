package com.gasparian.rob.feature.milestones.presentation

import com.gasparian.rob.feature.milestones.domain.model.CurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.Milestone
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MilestonesUiMapperTest {
    @Test
    fun `maps successful milestones result to content state`() {
        val state = Result.success(milestones()).toMilestonesUiState()

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("KMP", state.milestones?.currentFocus?.topics?.single())
        assertEquals("Room KMP", state.milestones?.recentMilestones?.single()?.title)
    }

    @Test
    fun `maps failed milestones result to error state`() {
        val state = Result.failure<Milestones>(IllegalStateException("Milestones failed")).toMilestonesUiState()

        assertFalse(state.isLoading)
        assertNull(state.milestones)
        assertEquals("Milestones failed", state.errorMessage)
    }

    private fun milestones() = Milestones(
        currentFocus =
        CurrentFocus(
            summary = "KMP migration prep.",
            topics = listOf("KMP"),
        ),
        recentMilestones =
        listOf(
            Milestone(
                id = "room-kmp",
                title = "Room KMP",
                description = "Structured persistence.",
                completedAt = LocalDate(2026, 5, 1),
                topics = listOf("Room"),
            ),
        ),
    )
}
