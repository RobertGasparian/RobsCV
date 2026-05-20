package com.gasparian.rob.feature.skills.data.mapper

import com.gasparian.rob.feature.skills.data.remote.SkillCategoryDto
import com.gasparian.rob.feature.skills.data.remote.SkillDto
import com.gasparian.rob.feature.skills.data.remote.SkillsResponseDto
import com.gasparian.rob.feature.skills.domain.model.SkillLevel
import com.gasparian.rob.feature.skills.domain.model.SkillProficiencyType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RcvSkillsMappersTest {
    @Test
    fun `toEntityGraph flattens skill contexts with stable ordering`() {
        val graph = skillsDto.toEntityGraph()

        assertEquals("android", graph.categories.single().id)
        assertEquals("kotlin", graph.skills.single().id)
        assertEquals(listOf(0, 1), graph.contexts.map { it.sortIndex })
        assertEquals(listOf("Production apps", "KMP migration"), graph.contexts.map { it.context })
    }

    @Test
    fun `toDomain attaches sorted contexts to each skill`() {
        val domain = skillsDto.toEntityGraph().toDomain()

        assertEquals("Android", domain.categories.single().name)
        assertEquals("Kotlin", domain.skills.single().name)
        assertEquals(SkillProficiencyType.LEVELED, domain.skills.single().proficiencyType)
        assertEquals(SkillLevel.EXPERT, domain.skills.single().level)
        assertEquals(listOf("Production apps", "KMP migration"), domain.skills.single().contexts)
    }
}

private val skillsDto = SkillsResponseDto(
    categories = listOf(SkillCategoryDto(id = "android", name = "Android")),
    skills = listOf(
        SkillDto(
            id = "kotlin",
            name = "Kotlin",
            categoryId = "android",
            proficiencyType = "leveled",
            level = "expert",
            yearsOfExperience = 8,
            isCore = true,
            contexts = listOf("Production apps", "KMP migration"),
        ),
    ),
)
