package com.gasparian.rob.feature.milestones.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface MilestonesDao {
    fun milestonesGraphFlow(): Flow<MilestonesEntityGraph?> = combine(
        currentFocusFlow(),
        currentFocusTopicsFlow(),
        milestonesFlow(),
        milestoneTopicsFlow(),
    ) { currentFocus, currentFocusTopics, milestones, milestoneTopics ->
        currentFocus ?: return@combine null
        MilestonesEntityGraph(
            currentFocus = currentFocus,
            currentFocusTopics = currentFocusTopics,
            milestones = milestones,
            milestoneTopics = milestoneTopics,
        )
    }

    @Transaction
    suspend fun getMilestonesGraph(): MilestonesEntityGraph? {
        val currentFocus = getCurrentFocus() ?: return null
        return MilestonesEntityGraph(
            currentFocus = currentFocus,
            currentFocusTopics = getCurrentFocusTopics(),
            milestones = getMilestones(),
            milestoneTopics = getMilestoneTopics(),
        )
    }

    @Transaction
    suspend fun replaceMilestones(
        graph: MilestonesEntityGraph,
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
        id: String = CurrentFocusEntity.DEFAULT_ID,
    ): CurrentFocusEntity?

    @Query("SELECT * FROM rcv_current_focus WHERE id = :id")
    fun currentFocusFlow(
        id: String = CurrentFocusEntity.DEFAULT_ID,
    ): Flow<CurrentFocusEntity?>

    @Query("SELECT * FROM rcv_current_focus_topic ORDER BY sortIndex")
    suspend fun getCurrentFocusTopics(): List<CurrentFocusTopicEntity>

    @Query("SELECT * FROM rcv_current_focus_topic ORDER BY sortIndex")
    fun currentFocusTopicsFlow(): Flow<List<CurrentFocusTopicEntity>>

    @Query("SELECT * FROM rcv_milestone ORDER BY completedAt DESC")
    suspend fun getMilestones(): List<MilestoneEntity>

    @Query("SELECT * FROM rcv_milestone ORDER BY completedAt DESC")
    fun milestonesFlow(): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM rcv_milestone_topic ORDER BY milestoneId, sortIndex")
    suspend fun getMilestoneTopics(): List<MilestoneTopicEntity>

    @Query("SELECT * FROM rcv_milestone_topic ORDER BY milestoneId, sortIndex")
    fun milestoneTopicsFlow(): Flow<List<MilestoneTopicEntity>>

    @Upsert
    suspend fun upsertCurrentFocus(
        currentFocus: CurrentFocusEntity,
    )

    @Upsert
    suspend fun upsertCurrentFocusTopics(
        topics: List<CurrentFocusTopicEntity>,
    )

    @Upsert
    suspend fun upsertMilestones(
        milestones: List<MilestoneEntity>,
    )

    @Upsert
    suspend fun upsertMilestoneTopics(
        topics: List<MilestoneTopicEntity>,
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
