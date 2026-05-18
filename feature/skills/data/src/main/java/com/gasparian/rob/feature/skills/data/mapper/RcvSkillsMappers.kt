package com.gasparian.rob.feature.skills.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.skills.data.local.RcvSkillCategoryEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillContextEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillsEntityGraph
import com.gasparian.rob.feature.skills.data.remote.RcvSkillsResponseDto
import com.gasparian.rob.feature.skills.domain.model.RcvSkill
import com.gasparian.rob.feature.skills.domain.model.RcvSkillCategory
import com.gasparian.rob.feature.skills.domain.model.RcvSkills

internal fun RcvSkillsResponseDto.toEntityGraph(): RcvSkillsEntityGraph = RcvSkillsEntityGraph(
    categories =
    categories.map { category ->
        RcvSkillCategoryEntity(
            id = category.id,
            name = category.name,
        )
    },
    skills =
    skills.map { skill ->
        RcvSkillEntity(
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
            RcvSkillContextEntity(
                skillId = skill.id,
                context = context,
                sortIndex = index,
            )
        }
    },
)

internal fun RcvSkillsEntityGraph.toDomain(): RcvSkills = RcvSkills(
    categories =
    categories.map { category ->
        RcvSkillCategory(
            id = category.id,
            name = category.name,
        )
    },
    skills =
    skills.map { skill ->
        RcvSkill(
            id = skill.id,
            name = skill.name,
            shortName = skill.shortName,
            categoryId = skill.categoryId,
            proficiencyType = skill.proficiencyType,
            level = skill.level,
            yearsOfExperience = skill.yearsOfExperience,
            isCore = skill.isCore,
            contexts =
            contexts
                .filter { context -> context.skillId == skill.id }
                .sortedBy(RcvSkillContextEntity::sortIndex)
                .map(RcvSkillContextEntity::context),
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())
