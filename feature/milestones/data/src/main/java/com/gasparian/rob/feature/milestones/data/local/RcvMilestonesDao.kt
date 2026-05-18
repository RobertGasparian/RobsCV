package com.gasparian.rob.feature.milestones.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface RcvMilestonesDao {
    fun milestonesGraphFlow(): Flow<RcvMilestonesEntityGraph?> = combine(
        currentFocusFlow(),
        currentFocusTopicsFlow(),
        milestonesFlow(),
        milestoneTopicsFlow(),
    ) { currentFocus, currentFocusTopics, milestones, milestoneTopics ->
        currentFocus ?: return@combine null
        RcvMilestonesEntityGraph(
            currentFocus = currentFocus,
            currentFocusTopics = currentFocusTopics,
            milestones = milestones,
            milestoneTopics = milestoneTopics,
        )
    }

    @Transaction
    suspend fun getMilestonesGraph(): RcvMilestonesEntityGraph? {
        val currentFocus = getCurrentFocus() ?: return null
        return RcvMilestonesEntityGraph(
            currentFocus = currentFocus,
            currentFocusTopics = getCurrentFocusTopics(),
            milestones = getMilestones(),
            milestoneTopics = getMilestoneTopics(),
        )
    }

    @Transaction
    suspend fun replaceMilestones(
        graph: RcvMilestonesEntityGraph,
    ) {
        clearMilestoneTopics()
        clearMilestones()
        clearCurrentFocusTopics()
        clearCurrentFocus()
        upsertCurrentFocus(graph.currentFocus)
        upsertCurrentFocusTopics(graph.currentFocusTopics)
        upsertMilestones(graph.milestones)
        upsertMilestoneTopics(graph.milestoneTopics)
    }

    @Query("SELECT * FROM rcv_current_focus WHERE id = :id")
    suspend fun getCurrentFocus(
        id: String = RcvCurrentFocusEntity.DEFAULT_ID,
    ): RcvCurrentFocusEntity?

    @Query("SELECT * FROM rcv_current_focus WHERE id = :id")
    fun currentFocusFlow(
        id: String = RcvCurrentFocusEntity.DEFAULT_ID,
    ): Flow<RcvCurrentFocusEntity?>

    @Query("SELECT * FROM rcv_current_focus_topic ORDER BY sortIndex")
    suspend fun getCurrentFocusTopics(): List<RcvCurrentFocusTopicEntity>

    @Query("SELECT * FROM rcv_current_focus_topic ORDER BY sortIndex")
    fun currentFocusTopicsFlow(): Flow<List<RcvCurrentFocusTopicEntity>>

    @Query("SELECT * FROM rcv_milestone ORDER BY completedAt DESC")
    suspend fun getMilestones(): List<RcvMilestoneEntity>

    @Query("SELECT * FROM rcv_milestone ORDER BY completedAt DESC")
    fun milestonesFlow(): Flow<List<RcvMilestoneEntity>>

    @Query("SELECT * FROM rcv_milestone_topic ORDER BY milestoneId, sortIndex")
    suspend fun getMilestoneTopics(): List<RcvMilestoneTopicEntity>

    @Query("SELECT * FROM rcv_milestone_topic ORDER BY milestoneId, sortIndex")
    fun milestoneTopicsFlow(): Flow<List<RcvMilestoneTopicEntity>>

    @Upsert
    suspend fun upsertCurrentFocus(
        currentFocus: RcvCurrentFocusEntity,
    )

    @Upsert
    suspend fun upsertCurrentFocusTopics(
        topics: List<RcvCurrentFocusTopicEntity>,
    )

    @Upsert
    suspend fun upsertMilestones(
        milestones: List<RcvMilestoneEntity>,
    )

    @Upsert
    suspend fun upsertMilestoneTopics(
        topics: List<RcvMilestoneTopicEntity>,
    )

    @Query("DELETE FROM rcv_current_focus")
    suspend fun clearCurrentFocus()

    @Query("DELETE FROM rcv_current_focus_topic")
    suspend fun clearCurrentFocusTopics()

    @Query("DELETE FROM rcv_milestone")
    suspend fun clearMilestones()

    @Query("DELETE FROM rcv_milestone_topic")
    suspend fun clearMilestoneTopics()
}
