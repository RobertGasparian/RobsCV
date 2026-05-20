package com.gasparian.rob.feature.experience.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.experience.data.local.ExperienceEntityGraph
import com.gasparian.rob.feature.experience.data.local.ExperienceHighlightEntity
import com.gasparian.rob.feature.experience.data.local.ExperienceRoleEntity
import com.gasparian.rob.feature.experience.data.remote.ExperienceResponseDto
import com.gasparian.rob.feature.experience.data.remote.ExperienceRoleDto
import com.gasparian.rob.feature.experience.domain.model.Experience
import com.gasparian.rob.feature.experience.domain.model.ExperienceRole
import com.gasparian.rob.feature.experience.domain.model.WorkArrangement
import kotlinx.datetime.LocalDate

internal fun ExperienceResponseDto.toEntityGraph(): ExperienceEntityGraph {
    val roles =
        roles.map { role ->
            ExperienceRoleEntity(
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

    return ExperienceEntityGraph(
        roles = roles,
        highlights =
        this.roles.flatMap { role ->
            val roleId = role.stableId()
            role.highlights.mapIndexed { index, highlight ->
                ExperienceHighlightEntity(
                    id = "$roleId:highlight:$index",
                    roleId = roleId,
                    text = highlight,
                    sortIndex = index,
                )
            }
        },
    )
}

internal fun ExperienceEntityGraph.toDomain(): Experience = Experience(
    roles =
    roles.map { role ->
        ExperienceRole(
            title = role.title,
            company = role.company,
            startDate = LocalDate.parse(role.startDate),
            endDate = role.endDate?.let(LocalDate.Companion::parse),
            location = role.location,
            workArrangement = role.workArrangement.toWorkArrangement(),
            summary = role.summary,
            highlights =
            highlights
                .filter { highlight -> highlight.roleId == role.id }
                .sortedBy(ExperienceHighlightEntity::sortIndex)
                .map(ExperienceHighlightEntity::text),
        )
    },
)

private fun ExperienceRoleDto.stableId(): String = listOf(company, title, startDate)
    .joinToString(separator = "-")
    .lowercase()
    .replace(Regex("[^a-z0-9]+"), "-")
    .trim('-')

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())

private fun String.toWorkArrangement(): WorkArrangement = when (lowercase()) {
    "remote" -> WorkArrangement.Remote
    "on_site", "onsite", "on-site" -> WorkArrangement.OnSite
    "hybrid" -> WorkArrangement.Hybrid
    else -> WorkArrangement.Unknown(rawValue = this)
}
