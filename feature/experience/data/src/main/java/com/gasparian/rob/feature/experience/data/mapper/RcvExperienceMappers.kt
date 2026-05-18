package com.gasparian.rob.feature.experience.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.experience.data.local.RcvExperienceEntityGraph
import com.gasparian.rob.feature.experience.data.local.RcvExperienceHighlightEntity
import com.gasparian.rob.feature.experience.data.local.RcvExperienceRoleEntity
import com.gasparian.rob.feature.experience.data.remote.RcvExperienceResponseDto
import com.gasparian.rob.feature.experience.data.remote.RcvExperienceRoleDto
import com.gasparian.rob.feature.experience.domain.model.RcvExperience
import com.gasparian.rob.feature.experience.domain.model.RcvExperienceRole

internal fun RcvExperienceResponseDto.toEntityGraph(): RcvExperienceEntityGraph {
    val roles =
        roles.map { role ->
            RcvExperienceRoleEntity(
                id = role.stableId(),
                title = role.title,
                company = role.company,
                startDate = role.startDate,
                endDate = role.endDate,
                location = role.location,
                workArrangement = role.workArrangement,
                summary = role.summary,
            )
        }

    return RcvExperienceEntityGraph(
        roles = roles,
        highlights =
        this.roles.flatMap { role ->
            val roleId = role.stableId()
            role.highlights.mapIndexed { index, highlight ->
                RcvExperienceHighlightEntity(
                    id = "$roleId:highlight:$index",
                    roleId = roleId,
                    text = highlight,
                    sortIndex = index,
                )
            }
        },
    )
}

internal fun RcvExperienceEntityGraph.toDomain(): RcvExperience = RcvExperience(
    roles =
    roles.map { role ->
        RcvExperienceRole(
            title = role.title,
            company = role.company,
            startDate = role.startDate,
            endDate = role.endDate,
            location = role.location,
            workArrangement = role.workArrangement,
            summary = role.summary,
            highlights =
            highlights
                .filter { highlight -> highlight.roleId == role.id }
                .sortedBy(RcvExperienceHighlightEntity::sortIndex)
                .map(RcvExperienceHighlightEntity::text),
        )
    },
)

private fun RcvExperienceRoleDto.stableId(): String = listOf(company, title, startDate)
    .joinToString(separator = "-")
    .lowercase()
    .replace(Regex("[^a-z0-9]+"), "-")
    .trim('-')

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())
