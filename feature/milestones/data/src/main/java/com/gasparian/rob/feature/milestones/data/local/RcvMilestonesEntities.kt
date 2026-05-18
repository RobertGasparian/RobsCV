package com.gasparian.rob.feature.milestones.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class RcvMilestonesEntityGraph(
    val currentFocus: RcvCurrentFocusEntity,
    val currentFocusTopics: List<RcvCurrentFocusTopicEntity>,
    val milestones: List<RcvMilestoneEntity>,
    val milestoneTopics: List<RcvMilestoneTopicEntity>,
)

@Entity(tableName = "rcv_current_focus")
data class RcvCurrentFocusEntity(
    @PrimaryKey val id: String = DEFAULT_ID,
    val summary: String,
    val updatedAtMillis: Long,
) {
    companion object {
        const val DEFAULT_ID = "current-focus"
    }
}

@Entity(
    tableName = "rcv_current_focus_topic",
    primaryKeys = ["focusId", "topic"],
)
data class RcvCurrentFocusTopicEntity(
    val focusId: String,
    val topic: String,
    val sortIndex: Int,
)

@Entity(tableName = "rcv_milestone")
data class RcvMilestoneEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val completedAt: String,
)

@Entity(
    tableName = "rcv_milestone_topic",
    primaryKeys = ["milestoneId", "topic"],
)
data class RcvMilestoneTopicEntity(
    val milestoneId: String,
    val topic: String,
    val sortIndex: Int,
)
