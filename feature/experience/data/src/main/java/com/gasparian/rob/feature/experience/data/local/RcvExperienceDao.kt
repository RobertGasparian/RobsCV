package com.gasparian.rob.feature.experience.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface RcvExperienceDao {
    fun experienceGraphFlow(): Flow<RcvExperienceEntityGraph> = combine(
        rolesFlow(),
        highlightsFlow(),
    ) { roles, highlights ->
        RcvExperienceEntityGraph(
            roles = roles,
            highlights = highlights,
        )
    }

    @Transaction
    suspend fun getExperienceGraph(): RcvExperienceEntityGraph = RcvExperienceEntityGraph(
        roles = getRoles(),
        highlights = getHighlights(),
    )

    @Transaction
    suspend fun replaceExperience(
        graph: RcvExperienceEntityGraph,
    ) {
        clearHighlights()
        clearRoles()
        upsertRoles(graph.roles)
        upsertHighlights(graph.highlights)
    }

    @Query("SELECT * FROM rcv_experience_role ORDER BY startDate DESC")
    suspend fun getRoles(): List<RcvExperienceRoleEntity>

    @Query("SELECT * FROM rcv_experience_role ORDER BY startDate DESC")
    fun rolesFlow(): Flow<List<RcvExperienceRoleEntity>>

    @Query("SELECT * FROM rcv_experience_highlight ORDER BY roleId, sortIndex")
    suspend fun getHighlights(): List<RcvExperienceHighlightEntity>

    @Query("SELECT * FROM rcv_experience_highlight ORDER BY roleId, sortIndex")
    fun highlightsFlow(): Flow<List<RcvExperienceHighlightEntity>>

    @Upsert
    suspend fun upsertRoles(
        roles: List<RcvExperienceRoleEntity>,
    )

    @Upsert
    suspend fun upsertHighlights(
        highlights: List<RcvExperienceHighlightEntity>,
    )

    @Query("DELETE FROM rcv_experience_role")
    suspend fun clearRoles()

    @Query("DELETE FROM rcv_experience_highlight")
    suspend fun clearHighlights()
}
