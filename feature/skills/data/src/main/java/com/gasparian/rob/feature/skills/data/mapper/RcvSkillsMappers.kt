package com.gasparian.rob.feature.skills.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.skills.data.local.SkillCategoryEntity
import com.gasparian.rob.feature.skills.data.local.SkillContextEntity
import com.gasparian.rob.feature.skills.data.local.SkillEntity
import com.gasparian.rob.feature.skills.data.local.SkillsEntityGraph
import com.gasparian.rob.feature.skills.data.remote.SkillsResponseDto
import com.gasparian.rob.feature.skills.domain.model.Skill
import com.gasparian.rob.feature.skills.domain.model.SkillCategory
import com.gasparian.rob.feature.skills.domain.model.SkillLevel
import com.gasparian.rob.feature.skills.domain.model.SkillProficiencyType
import com.gasparian.rob.feature.skills.domain.model.Skills

internal fun SkillsResponseDto.toEntityGraph(): SkillsEntityGraph = SkillsEntityGraph(
    categories =
    categories.map { category ->
        SkillCategoryEntity(
            id = category.id,
            name = category.name,
        )
    },
    skills =
    skills.map { skill ->
        SkillEntity(
            id = skill.id,
            name = skill.name,
            shortName = skill.shortName,
            categoryId = skill.categoryId,
            proficiencyType = skill.proficiencyType,
            level = skill.level,
            yearsOfExperience = skill.yearsOfExperience,
            isCore = skill.isCore,
        )
    },
    contexts =
    skills.flatMap { skill ->
        skill.contexts.mapIndexed { index, context ->
            SkillContextEntity(
                skillId = skill.id,
                context = context,
                sortIndex = index,
            )
        }
    },
)

internal fun SkillsEntityGraph.toDomain(): Skills = Skills(
    categories =
    categories.map { category ->
        SkillCategory(
            id = category.id,
            name = category.name,
        )
    },
    skills =
    skills.map { skill ->
        Skill(
            id = skill.id,
            name = skill.name,
            shortName = skill.shortName,
            categoryId = skill.categoryId,
            proficiencyType = skill.proficiencyType.toSkillProficiencyType(),
            level = skill.level?.toSkillLevel(),
            yearsOfExperience = skill.yearsOfExperience,
            isCore = skill.isCore,
            contexts =
            contexts
                .filter { context -> context.skillId == skill.id }
                .sortedBy(SkillContextEntity::sortIndex)
                .map(SkillContextEntity::context),
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())

private fun String.toSkillProficiencyType(): SkillProficiencyType = when (lowercase()) {
    "leveled" -> SkillProficiencyType.LEVELED
    "used" -> SkillProficiencyType.USED
    else -> SkillProficiencyType.UNKNOWN
}

private fun String.toSkillLevel(): SkillLevel = when (lowercase()) {
    "expert" -> SkillLevel.EXPERT
    "advanced" -> SkillLevel.ADVANCED
    "intermediate" -> SkillLevel.INTERMEDIATE
    "familiar" -> SkillLevel.FAMILIAR
    else -> SkillLevel.UNKNOWN
}
