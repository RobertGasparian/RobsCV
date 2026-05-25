package com.gasparian.rob.feature.skills.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.skills.data.local.SkillCategoryEntity
import com.gasparian.rob.feature.skills.data.local.SkillContextEntity
import com.gasparian.rob.feature.skills.data.local.SkillEntity
import com.gasparian.rob.feature.skills.data.local.SkillsDao
import com.gasparian.rob.feature.skills.data.local.SkillsEntityGraph
import com.gasparian.rob.feature.skills.data.mapper.toEntityGraph
import com.gasparian.rob.feature.skills.data.remote.SkillCategoryDto
import com.gasparian.rob.feature.skills.data.remote.SkillDto
import com.gasparian.rob.feature.skills.data.remote.SkillsRemoteDataSource
import com.gasparian.rob.feature.skills.data.remote.SkillsResponseDto
import com.gasparian.rob.feature.skills.domain.model.SkillLevel
import com.gasparian.rob.feature.skills.domain.model.SkillProficiencyType
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
        val remoteDataSource = mockk<SkillsRemoteDataSource>()
        coEvery { remoteDataSource.getSkills() } returns RcvNetworkResult.Success(skillsResponse)
        val repository = NetworkBackedRcvSkillsRepository(remoteDataSource = remoteDataSource, skillsDao = dao)

        repository.sync()

        repository.skills.test {
            val item = awaitItem()

            assertEquals("Kotlin", item.getOrThrow().skills.single().name)
            assertEquals(SkillProficiencyType.LEVELED, item.getOrThrow().skills.single().proficiencyType)
            assertEquals(SkillLevel.EXPERT, item.getOrThrow().skills.single().level)
            assertEquals("kotlin", dao.lastReplacedGraph?.skills?.single()?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `skills emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvSkillsDao(initialGraph = skillsResponse.toEntityGraph())
        val remoteDataSource = mockk<SkillsRemoteDataSource>()
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
        val remoteDataSource = mockk<SkillsRemoteDataSource>()
        coEvery { remoteDataSource.getSkills() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvSkillsRepository(remoteDataSource = remoteDataSource, skillsDao = dao)

        repository.skills.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearCache clears dao cache without syncing remote data`() = runTest {
        val dao = FakeRcvSkillsDao(initialGraph = skillsResponse.toEntityGraph())
        val remoteDataSource = mockk<SkillsRemoteDataSource>()
        val repository = NetworkBackedRcvSkillsRepository(remoteDataSource = remoteDataSource, skillsDao = dao)

        repository.clearCache()

        assertEquals(1, dao.clearSkillsCacheCallCount)
        assertEquals(null, dao.lastReplacedGraph)
    }
}

private class FakeRcvSkillsDao(
    initialGraph: SkillsEntityGraph = SkillsEntityGraph(emptyList(), emptyList(), emptyList()),
) : SkillsDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: SkillsEntityGraph? = null
    var clearSkillsCacheCallCount = 0

    override fun skillsGraphFlow(): Flow<SkillsEntityGraph> = graphFlow

    override suspend fun replaceSkills(graph: SkillsEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getSkillsGraph(): SkillsEntityGraph = graphFlow.value
    override suspend fun clearSkillsCache() {
        clearSkillsCacheCallCount++
        graphFlow.value = SkillsEntityGraph(emptyList(), emptyList(), emptyList())
    }

    override suspend fun getCategories(): List<SkillCategoryEntity> = error("Unused")
    override fun categoriesFlow(): Flow<List<SkillCategoryEntity>> = error("Unused")
    override suspend fun getSkills(): List<SkillEntity> = error("Unused")
    override fun skillsFlow(): Flow<List<SkillEntity>> = error("Unused")
    override suspend fun getContexts(): List<SkillContextEntity> = error("Unused")
    override fun contextsFlow(): Flow<List<SkillContextEntity>> = error("Unused")
    override suspend fun upsertCategories(categories: List<SkillCategoryEntity>) = error("Unused")
    override suspend fun upsertSkills(skills: List<SkillEntity>) = error("Unused")
    override suspend fun upsertContexts(contexts: List<SkillContextEntity>) = error("Unused")
    override suspend fun clearCategories() = error("Unused")
    override suspend fun clearSkills() = error("Unused")
    override suspend fun clearContexts() = error("Unused")
}

private val skillsResponse = SkillsResponseDto(
    categories = listOf(SkillCategoryDto(id = "android", name = "Android")),
    skills = listOf(
        SkillDto(
            id = "kotlin",
            name = "Kotlin",
            categoryId = "android",
            proficiencyType = "leveled",
            level = "expert",
            yearsOfExperience = 8,
            isCore = true,
            contexts = emptyList(),
        ),
    ),
)
