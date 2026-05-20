package com.gasparian.rob.feature.skills.presentation

import com.gasparian.rob.feature.skills.domain.model.Skill
import com.gasparian.rob.feature.skills.domain.model.SkillCategory
import com.gasparian.rob.feature.skills.domain.model.SkillLevel
import com.gasparian.rob.feature.skills.domain.model.SkillProficiencyType
import com.gasparian.rob.feature.skills.domain.model.Skills
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class SkillsUiMapperTest {
    @Test
    fun `maps successful skills result to content state`() {
        val state = Result.success(skills()).toSkillsUiState()

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Android", state.skills?.categories?.single()?.name)
        assertEquals(SkillLevelUiModel.EXPERT, state.skills?.skills?.single()?.level)
    }

    @Test
    fun `maps failed skills result to error state`() {
        val state = Result.failure<Skills>(IllegalStateException("Skills failed")).toSkillsUiState()

        assertFalse(state.isLoading)
        assertNull(state.skills)
        assertEquals("Skills failed", state.errorMessage)
    }

    private fun skills() = Skills(
        categories =
        listOf(
            SkillCategory(
                id = "android",
                name = "Android",
            ),
        ),
        skills =
        listOf(
            Skill(
                id = "kotlin",
                name = "Kotlin",
                shortName = null,
                categoryId = "android",
                proficiencyType = SkillProficiencyType.LEVELED,
                level = SkillLevel.EXPERT,
                yearsOfExperience = 8,
                isCore = true,
                contexts = listOf("Android"),
            ),
        ),
    )
}
