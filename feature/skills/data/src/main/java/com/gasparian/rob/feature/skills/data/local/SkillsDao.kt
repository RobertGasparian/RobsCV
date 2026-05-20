package com.gasparian.rob.feature.skills.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface SkillsDao {
    fun skillsGraphFlow(): Flow<SkillsEntityGraph> = combine(
        categoriesFlow(),
        skillsFlow(),
        contextsFlow(),
    ) { categories, skills, contexts ->
        SkillsEntityGraph(
            categories = categories,
            skills = skills,
            contexts = contexts,
        )
    }

    @Transaction
    suspend fun getSkillsGraph(): SkillsEntityGraph = SkillsEntityGraph(
        categories = getCategories(),
        skills = getSkills(),
        contexts = getContexts(),
    )

    @Transaction
    suspend fun replaceSkills(
        graph: SkillsEntityGraph,
    ) {
        clearContexts()
        clearSkills()
        clearCategories()
        upsertCategories(graph.categories)
        upsertSkills(graph.skills)
        upsertContexts(graph.contexts)
    }

    @Query("SELECT * FROM rcv_skill_category ORDER BY name")
    suspend fun getCategories(): List<SkillCategoryEntity>

    @Query("SELECT * FROM rcv_skill_category ORDER BY name")
    fun categoriesFlow(): Flow<List<SkillCategoryEntity>>

    @Query("SELECT * FROM rcv_skill ORDER BY name")
    suspend fun getSkills(): List<SkillEntity>

    @Query("SELECT * FROM rcv_skill ORDER BY name")
    fun skillsFlow(): Flow<List<SkillEntity>>

    @Query("SELECT * FROM rcv_skill_context ORDER BY skillId, sortIndex")
    suspend fun getContexts(): List<SkillContextEntity>

    @Query("SELECT * FROM rcv_skill_context ORDER BY skillId, sortIndex")
    fun contextsFlow(): Flow<List<SkillContextEntity>>

    @Upsert
    suspend fun upsertCategories(
        categories: List<SkillCategoryEntity>,
    )

    @Upsert
    suspend fun upsertSkills(
        skills: List<SkillEntity>,
    )

    @Upsert
    suspend fun upsertContexts(
        contexts: List<SkillContextEntity>,
    )

    @Query("DELETE FROM rcv_skill_category")
    suspend fun clearCategories()

    @Query("DELETE FROM rcv_skill")
    suspend fun clearSkills()

    @Query("DELETE FROM rcv_skill_context")
    suspend fun clearContexts()
}
