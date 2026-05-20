package com.gasparian.rob.feature.milestones.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class MilestonesEntityGraph(
    val currentFocus: CurrentFocusEntity,
    val currentFocusTopics: List<CurrentFocusTopicEntity>,
    val milestones: List<MilestoneEntity>,
    val milestoneTopics: List<MilestoneTopicEntity>,
)

@Entity(tableName = "rcv_current_focus")
data class CurrentFocusEntity(
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
data class CurrentFocusTopicEntity(
    val focusId: String,
    val topic: String,
    val sortIndex: Int,
)

@Entity(tableName = "rcv_milestone")
data class MilestoneEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val completedAt: String,
)

@Entity(
    tableName = "rcv_milestone_topic",
    primaryKeys = ["milestoneId", "topic"],
)
data class MilestoneTopicEntity(
    val milestoneId: String,
    val topic: String,
    val sortIndex: Int,
)
