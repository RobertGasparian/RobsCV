package com.gasparian.rob.feature.education.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.education.data.local.RcvEducationDao
import com.gasparian.rob.feature.education.data.local.RcvEducationEntityGraph
import com.gasparian.rob.feature.education.data.local.RcvEducationItemEntity
import com.gasparian.rob.feature.education.data.local.RcvEducationLocationEntity
import com.gasparian.rob.feature.education.data.local.RcvInstitutionEntity
import com.gasparian.rob.feature.education.data.mapper.toEntityGraph
import com.gasparian.rob.feature.education.data.remote.RcvEducationItemDto
import com.gasparian.rob.feature.education.data.remote.RcvEducationLocationDto
import com.gasparian.rob.feature.education.data.remote.RcvEducationProgramDto
import com.gasparian.rob.feature.education.data.remote.RcvEducationRemoteDataSource
import com.gasparian.rob.feature.education.data.remote.RcvEducationResponseDto
import com.gasparian.rob.feature.education.data.remote.RcvInstitutionDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkBackedRcvEducationRepositoryTest {
    @Test
    fun `education syncs remote data into dao before emitting`() = runTest {
        val dao = FakeRcvEducationDao()
        val remoteDataSource = mockk<RcvEducationRemoteDataSource>()
        coEvery { remoteDataSource.getEducation() } returns RcvNetworkResult.Success(educationResponse)
        val repository = NetworkBackedRcvEducationRepository(remoteDataSource = remoteDataSource, educationDao = dao)

        repository.education.test {
            val item = awaitItem()

            assertEquals("Yerevan State University", item.getOrThrow().institutions.single().name)
            assertEquals("ysu", dao.lastReplacedGraph?.institutions?.single()?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `education emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvEducationDao(initialGraph = educationResponse.toEntityGraph())
        val remoteDataSource = mockk<RcvEducationRemoteDataSource>()
        coEvery { remoteDataSource.getEducation() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvEducationRepository(remoteDataSource = remoteDataSource, educationDao = dao)

        repository.education.test {
            val item = awaitItem()

            assertEquals("Yerevan State University", item.getOrThrow().institutions.single().name)
            assertEquals(null, dao.lastReplacedGraph)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `education emits failure when remote sync fails and cache is empty`() = runTest {
        val dao = FakeRcvEducationDao()
        val remoteDataSource = mockk<RcvEducationRemoteDataSource>()
        coEvery { remoteDataSource.getEducation() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvEducationRepository(remoteDataSource = remoteDataSource, educationDao = dao)

        repository.education.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeRcvEducationDao(
    initialGraph: RcvEducationEntityGraph = RcvEducationEntityGraph(emptyList(), emptyList(), emptyList()),
) : RcvEducationDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: RcvEducationEntityGraph? = null

    override fun educationGraphFlow(): Flow<RcvEducationEntityGraph> = graphFlow

    override suspend fun replaceEducation(graph: RcvEducationEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getEducationGraph(): RcvEducationEntityGraph = graphFlow.value
    override suspend fun getInstitutions(): List<RcvInstitutionEntity> = error("Unused")
    override fun institutionsFlow(): Flow<List<RcvInstitutionEntity>> = error("Unused")
    override suspend fun getLocations(): List<RcvEducationLocationEntity> = error("Unused")
    override fun locationsFlow(): Flow<List<RcvEducationLocationEntity>> = error("Unused")
    override suspend fun getItems(): List<RcvEducationItemEntity> = error("Unused")
    override fun itemsFlow(): Flow<List<RcvEducationItemEntity>> = error("Unused")
    override suspend fun upsertInstitutions(institutions: List<RcvInstitutionEntity>) = error("Unused")
    override suspend fun upsertLocations(locations: List<RcvEducationLocationEntity>) = error("Unused")
    override suspend fun upsertItems(items: List<RcvEducationItemEntity>) = error("Unused")
    override suspend fun clearInstitutions() = error("Unused")
    override suspend fun clearLocations() = error("Unused")
    override suspend fun clearItems() = error("Unused")
}

private val educationResponse = RcvEducationResponseDto(
    institutions = listOf(
        RcvInstitutionDto(
            id = "ysu",
            name = "Yerevan State University",
            shortName = "YSU",
            type = "university",
            description = "Public university in Armenia.",
            websiteUrl = "https://www.ysu.am",
            location = RcvEducationLocationDto(city = "Yerevan", country = "Armenia"),
        ),
    ),
    items = listOf(
        RcvEducationItemDto(
            id = "ysu-management-master",
            institutionId = "ysu",
            program = RcvEducationProgramDto(
                name = "Management",
                credential = "Master's Degree",
                fieldOfStudy = "Management",
            ),
            startDate = "2014-09-01",
            endDate = "2016-06-30",
            status = "completed",
        ),
    ),
)
