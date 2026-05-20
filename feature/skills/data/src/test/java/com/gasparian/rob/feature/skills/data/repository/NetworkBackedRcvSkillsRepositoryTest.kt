package com.gasparian.rob.feature.skills.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.skills.data.local.RcvSkillCategoryEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillContextEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillEntity
import com.gasparian.rob.feature.skills.data.local.RcvSkillsDao
import com.gasparian.rob.feature.skills.data.local.RcvSkillsEntityGraph
import com.gasparian.rob.feature.skills.data.mapper.toEntityGraph
import com.gasparian.rob.feature.skills.data.remote.RcvSkillCategoryDto
import com.gasparian.rob.feature.skills.data.remote.RcvSkillDto
import com.gasparian.rob.feature.skills.data.remote.RcvSkillsRemoteDataSource
import com.gasparian.rob.feature.skills.data.remote.RcvSkillsResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkBackedRcvSkillsRepositoryTest {
    @Test
    fun `skills syncs remote data into dao before emitting`() = runTest {
        val dao = FakeRcvSkillsDao()
        val remoteDataSource = mockk<RcvSkillsRemoteDataSource>()
        coEvery { remoteDataSource.getSkills() } returns RcvNetworkResult.Success(skillsResponse)
        val repository = NetworkBackedRcvSkillsRepository(remoteDataSource = remoteDataSource, skillsDao = dao)

        repository.skills.test {
            val item = awaitItem()

            assertEquals("Kotlin", item.getOrThrow().skills.single().name)
            assertEquals("kotlin", dao.lastReplacedGraph?.skills?.single()?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `skills emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvSkillsDao(initialGraph = skillsResponse.toEntityGraph())
        val remoteDataSource = mockk<RcvSkillsRemoteDataSource>()
        coEvery { remoteDataSource.getSkills() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvSkillsRepository(remoteDataSource = remoteDataSource, skillsDao = dao)

        repository.skills.test {
            val item = awaitItem()

            assertEquals("Kotlin", item.getOrThrow().skills.single().name)
            assertEquals(null, dao.lastReplacedGraph)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `skills emits failure when remote sync fails and cache is empty`() = runTest {
        val dao = FakeRcvSkillsDao()
        val remoteDataSource = mockk<RcvSkillsRemoteDataSource>()
        coEvery { remoteDataSource.getSkills() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvSkillsRepository(remoteDataSource = remoteDataSource, skillsDao = dao)

        repository.skills.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeRcvSkillsDao(
    initialGraph: RcvSkillsEntityGraph = RcvSkillsEntityGraph(emptyList(), emptyList(), emptyList()),
) : RcvSkillsDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: RcvSkillsEntityGraph? = null

    override fun skillsGraphFlow(): Flow<RcvSkillsEntityGraph> = graphFlow

    override suspend fun replaceSkills(graph: RcvSkillsEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getSkillsGraph(): RcvSkillsEntityGraph = graphFlow.value
    override suspend fun getCategories(): List<RcvSkillCategoryEntity> = error("Unused")
    override fun categoriesFlow(): Flow<List<RcvSkillCategoryEntity>> = error("Unused")
    override suspend fun getSkills(): List<RcvSkillEntity> = error("Unused")
    override fun skillsFlow(): Flow<List<RcvSkillEntity>> = error("Unused")
    override suspend fun getContexts(): List<RcvSkillContextEntity> = error("Unused")
    override fun contextsFlow(): Flow<List<RcvSkillContextEntity>> = error("Unused")
    override suspend fun upsertCategories(categories: List<RcvSkillCategoryEntity>) = error("Unused")
    override suspend fun upsertSkills(skills: List<RcvSkillEntity>) = error("Unused")
    override suspend fun upsertContexts(contexts: List<RcvSkillContextEntity>) = error("Unused")
    override suspend fun clearCategories() = error("Unused")
    override suspend fun clearSkills() = error("Unused")
    override suspend fun clearContexts() = error("Unused")
}

private val skillsResponse = RcvSkillsResponseDto(
    categories = listOf(RcvSkillCategoryDto(id = "android", name = "Android")),
    skills = listOf(
        RcvSkillDto(
            id = "kotlin",
            name = "Kotlin",
            categoryId = "android",
            proficiencyType = "graded",
            level = "expert",
            yearsOfExperience = 8,
            isCore = true,
            contexts = emptyList(),
        ),
    ),
)
