package com.gasparian.rob.feature.experience.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface ExperienceDao {
    fun experienceGraphFlow(): Flow<ExperienceEntityGraph> = combine(
        rolesFlow(),
        highlightsFlow(),
    ) { roles, highlights ->
        ExperienceEntityGraph(
            roles = roles,
            highlights = highlights,
        )
    }

    @Transaction
    suspend fun getExperienceGraph(): ExperienceEntityGraph = ExperienceEntityGraph(
        roles = getRoles(),
        highlights = getHighlights(),
    )

    @Transaction
    suspend fun replaceExperience(
        graph: ExperienceEntityGraph,
    ) {
        clearExperienceCache()
        upsertRoles(graph.roles)
        upsertHighlights(graph.highlights)
    }

    @Transaction
    suspend fun clearExperienceCache() {
        clearHighlights()
        clearRoles()
    }

    @Query("SELECT * FROM rcv_experience_role ORDER BY startDate DESC")
    suspend fun getRoles(): List<ExperienceRoleEntity>

    @Query("SELECT * FROM rcv_experience_role ORDER BY startDate DESC")
    fun rolesFlow(): Flow<List<ExperienceRoleEntity>>

    @Query("SELECT * FROM rcv_experience_highlight ORDER BY roleId, sortIndex")
    suspend fun getHighlights(): List<ExperienceHighlightEntity>

    @Query("SELECT * FROM rcv_experience_highlight ORDER BY roleId, sortIndex")
    fun highlightsFlow(): Flow<List<ExperienceHighlightEntity>>

    @Upsert
    suspend fun upsertRoles(
        roles: List<ExperienceRoleEntity>,
    )

    @Upsert
    suspend fun upsertHighlights(
        highlights: List<ExperienceHighlightEntity>,
    )

    @Query("DELETE FROM rcv_experience_role")
    suspend fun clearRoles()

    @Query("DELETE FROM rcv_experience_highlight")
    suspend fun clearHighlights()
}
