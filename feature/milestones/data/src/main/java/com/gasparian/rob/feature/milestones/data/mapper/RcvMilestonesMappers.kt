package com.gasparian.rob.feature.milestones.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.milestones.data.local.CurrentFocusEntity
import com.gasparian.rob.feature.milestones.data.local.CurrentFocusTopicEntity
import com.gasparian.rob.feature.milestones.data.local.MilestoneEntity
import com.gasparian.rob.feature.milestones.data.local.MilestoneTopicEntity
import com.gasparian.rob.feature.milestones.data.local.MilestonesEntityGraph
import com.gasparian.rob.feature.milestones.data.remote.MilestonesResponseDto
import com.gasparian.rob.feature.milestones.domain.model.CurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.Milestone
import com.gasparian.rob.feature.milestones.domain.model.Milestones
import kotlinx.datetime.LocalDate

internal fun MilestonesResponseDto.toEntityGraph(
    updatedAtMillis: Long,
): MilestonesEntityGraph = MilestonesEntityGraph(
    currentFocus =
    CurrentFocusEntity(
        summary = currentFocus.summary,
        updatedAtMillis = updatedAtMillis,
    ),
    currentFocusTopics =
    currentFocus.topics.mapIndexed { index, topic ->
        CurrentFocusTopicEntity(
            focusId = CurrentFocusEntity.DEFAULT_ID,
            topic = topic,
            sortIndex = index,
        )
    },
    milestones =
    recentMilestones.map { milestone ->
        MilestoneEntity(
            id = milestone.id,
            title = milestone.title,
            description = milestone.description,
            completedAt = milestone.completedAt,
        )
    },
    milestoneTopics =
    recentMilestones.flatMap { milestone ->
        milestone.topics.mapIndexed { index, topic ->
            MilestoneTopicEntity(
                milestoneId = milestone.id,
                topic = topic,
                sortIndex = index,
            )
        }
    },
)

internal fun MilestonesEntityGraph.toDomain(): Milestones = Milestones(
    currentFocus =
    CurrentFocus(
        summary = currentFocus.summary,
        topics =
        currentFocusTopics
            .sortedBy(CurrentFocusTopicEntity::sortIndex)
            .map(CurrentFocusTopicEntity::topic),
    ),
    recentMilestones =
    milestones.map { milestone ->
        Milestone(
            id = milestone.id,
            title = milestone.title,
            description = milestone.description,
            completedAt = LocalDate.parse(milestone.completedAt),
            topics =
            milestoneTopics
                .filter { topic -> topic.milestoneId == milestone.id }
                .sortedBy(MilestoneTopicEntity::sortIndex)
                .map(MilestoneTopicEntity::topic),
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())
