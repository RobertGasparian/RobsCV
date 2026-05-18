package com.gasparian.rob.feature.skills.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface RcvSkillsDao {
    fun skillsGraphFlow(): Flow<RcvSkillsEntityGraph> = combine(
        categoriesFlow(),
        skillsFlow(),
        contextsFlow(),
    ) { categories, skills, contexts ->
        RcvSkillsEntityGraph(
            categories = categories,
            skills = skills,
            contexts = contexts,
        )
    }

    @Transaction
    suspend fun getSkillsGraph(): RcvSkillsEntityGraph = RcvSkillsEntityGraph(
        categories = getCategories(),
        skills = getSkills(),
        contexts = getContexts(),
    )

    @Transaction
    suspend fun replaceSkills(
        graph: RcvSkillsEntityGraph,
    ) {
        clearContexts()
        clearSkills()
        clearCategories()
        upsertCategories(graph.categories)
        upsertSkills(graph.skills)
        upsertContexts(graph.contexts)
    }

    @Query("SELECT * FROM rcv_skill_category ORDER BY name")
    suspend fun getCategories(): List<RcvSkillCategoryEntity>

    @Query("SELECT * FROM rcv_skill_category ORDER BY name")
    fun categoriesFlow(): Flow<List<RcvSkillCategoryEntity>>

    @Query("SELECT * FROM rcv_skill ORDER BY name")
    suspend fun getSkills(): List<RcvSkillEntity>

    @Query("SELECT * FROM rcv_skill ORDER BY name")
    fun skillsFlow(): Flow<List<RcvSkillEntity>>

    @Query("SELECT * FROM rcv_skill_context ORDER BY skillId, sortIndex")
    suspend fun getContexts(): List<RcvSkillContextEntity>

    @Query("SELECT * FROM rcv_skill_context ORDER BY skillId, sortIndex")
    fun contextsFlow(): Flow<List<RcvSkillContextEntity>>

    @Upsert
    suspend fun upsertCategories(
        categories: List<RcvSkillCategoryEntity>,
    )

    @Upsert
    suspend fun upsertSkills(
        skills: List<RcvSkillEntity>,
    )

    @Upsert
    suspend fun upsertContexts(
        contexts: List<RcvSkillContextEntity>,
    )

    @Query("DELETE FROM rcv_skill_category")
    suspend fun clearCategories()

    @Query("DELETE FROM rcv_skill")
    suspend fun clearSkills()

    @Query("DELETE FROM rcv_skill_context")
    suspend fun clearContexts()
}
