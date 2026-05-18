package com.gasparian.rob.feature.milestones.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.milestones.data.local.RcvCurrentFocusEntity
import com.gasparian.rob.feature.milestones.data.local.RcvCurrentFocusTopicEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestoneEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestoneTopicEntity
import com.gasparian.rob.feature.milestones.data.local.RcvMilestonesEntityGraph
import com.gasparian.rob.feature.milestones.data.remote.RcvMilestonesResponseDto
import com.gasparian.rob.feature.milestones.domain.model.RcvCurrentFocus
import com.gasparian.rob.feature.milestones.domain.model.RcvMilestone
import com.gasparian.rob.feature.milestones.domain.model.RcvMilestones

internal fun RcvMilestonesResponseDto.toEntityGraph(
    updatedAtMillis: Long,
): RcvMilestonesEntityGraph = RcvMilestonesEntityGraph(
    currentFocus =
    RcvCurrentFocusEntity(
        summary = currentFocus.summary,
        updatedAtMillis = updatedAtMillis,
    ),
    currentFocusTopics =
    currentFocus.topics.mapIndexed { index, topic ->
        RcvCurrentFocusTopicEntity(
            focusId = RcvCurrentFocusEntity.DEFAULT_ID,
            topic = topic,
            sortIndex = index,
        )
    },
    milestones =
    recentMilestones.map { milestone ->
        RcvMilestoneEntity(
            id = milestone.id,
            title = milestone.title,
            description = milestone.description,
            completedAt = milestone.completedAt,
        )
    },
    milestoneTopics =
    recentMilestones.flatMap { milestone ->
        milestone.topics.mapIndexed { index, topic ->
            RcvMilestoneTopicEntity(
                milestoneId = milestone.id,
                topic = topic,
                sortIndex = index,
            )
        }
    },
)

internal fun RcvMilestonesEntityGraph.toDomain(): RcvMilestones = RcvMilestones(
    currentFocus =
    RcvCurrentFocus(
        summary = currentFocus.summary,
        topics =
        currentFocusTopics
            .sortedBy(RcvCurrentFocusTopicEntity::sortIndex)
            .map(RcvCurrentFocusTopicEntity::topic),
    ),
    recentMilestones =
    milestones.map { milestone ->
        RcvMilestone(
            id = milestone.id,
            title = milestone.title,
            description = milestone.description,
            completedAt = milestone.completedAt,
            topics =
            milestoneTopics
                .filter { topic -> topic.milestoneId == milestone.id }
                .sortedBy(RcvMilestoneTopicEntity::sortIndex)
                .map(RcvMilestoneTopicEntity::topic),
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())
