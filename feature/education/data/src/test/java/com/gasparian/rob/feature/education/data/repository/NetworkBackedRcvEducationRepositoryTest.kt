package com.gasparian.rob.feature.education.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.education.data.local.EducationDao
import com.gasparian.rob.feature.education.data.local.EducationEntityGraph
import com.gasparian.rob.feature.education.data.local.EducationEntityReadGraph
import com.gasparian.rob.feature.education.data.local.EducationItemEntity
import com.gasparian.rob.feature.education.data.local.EducationLocationEntity
import com.gasparian.rob.feature.education.data.local.InstitutionEntity
import com.gasparian.rob.feature.education.data.local.InstitutionWithEducationEntity
import com.gasparian.rob.feature.education.data.mapper.toEntityGraph
import com.gasparian.rob.feature.education.data.remote.EducationItemDto
import com.gasparian.rob.feature.education.data.remote.EducationLocationDto
import com.gasparian.rob.feature.education.data.remote.EducationProgramDto
import com.gasparian.rob.feature.education.data.remote.EducationRemoteDataSource
import com.gasparian.rob.feature.education.data.remote.EducationResponseDto
import com.gasparian.rob.feature.education.data.remote.InstitutionDto
import com.gasparian.rob.feature.education.domain.model.EducationStatus
import com.gasparian.rob.feature.education.domain.model.InstitutionType
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
        val remoteDataSource = mockk<EducationRemoteDataSource>()
        coEvery { remoteDataSource.getEducation() } returns RcvNetworkResult.Success(educationResponse)
        val repository = NetworkBackedRcvEducationRepository(remoteDataSource = remoteDataSource, educationDao = dao)

        repository.sync()

        repository.education.test {
            val item = awaitItem()

            assertEquals("Yerevan State University", item.getOrThrow().institutions.single().name)
            assertEquals(InstitutionType.PublicUniversity, item.getOrThrow().institutions.single().type)
            assertEquals(EducationStatus.Completed, item.getOrThrow().items.single().status)
            assertEquals("ysu", dao.lastReplacedGraph?.institutions?.single()?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `education emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvEducationDao(initialGraph = educationResponse.toEntityGraph())
        val remoteDataSource = mockk<EducationRemoteDataSource>()
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
        val remoteDataSource = mockk<EducationRemoteDataSource>()
        coEvery { remoteDataSource.getEducation() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvEducationRepository(remoteDataSource = remoteDataSource, educationDao = dao)

        repository.education.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearCache clears dao cache without syncing remote data`() = runTest {
        val dao = FakeRcvEducationDao(initialGraph = educationResponse.toEntityGraph())
        val remoteDataSource = mockk<EducationRemoteDataSource>()
        val repository = NetworkBackedRcvEducationRepository(remoteDataSource = remoteDataSource, educationDao = dao)

        repository.clearCache()

        assertEquals(1, dao.clearEducationCacheCallCount)
        assertEquals(null, dao.lastReplacedGraph)
    }
}

private class FakeRcvEducationDao(
    initialGraph: EducationEntityGraph = EducationEntityGraph(emptyList(), emptyList(), emptyList()),
) : EducationDao {
    private val graphFlow = MutableStateFlow(initialGraph.toReadGraph())
    private var graph = initialGraph
    var lastReplacedGraph: EducationEntityGraph? = null
    var clearEducationCacheCallCount = 0

    override fun educationGraphFlow(): Flow<EducationEntityReadGraph> = graphFlow

    override suspend fun replaceEducation(graph: EducationEntityGraph) {
        this.graph = graph
        lastReplacedGraph = graph
        graphFlow.value = graph.toReadGraph()
    }

    override suspend fun getEducationGraph(): EducationEntityGraph = graph
    override suspend fun clearEducationCache() {
        clearEducationCacheCallCount++
        graph = EducationEntityGraph(emptyList(), emptyList(), emptyList())
        graphFlow.value = graph.toReadGraph()
    }

    override suspend fun getInstitutions(): List<InstitutionEntity> = error("Unused")
    override fun institutionsWithEducationFlow(): Flow<List<InstitutionWithEducationEntity>> = error("Unused")
    override suspend fun getInstitutionsWithEducation(): List<InstitutionWithEducationEntity> = error("Unused")
    override fun institutionsFlow(): Flow<List<InstitutionEntity>> = error("Unused")
    override suspend fun getLocations(): List<EducationLocationEntity> = error("Unused")
    override fun locationsFlow(): Flow<List<EducationLocationEntity>> = error("Unused")
    override suspend fun getItems(): List<EducationItemEntity> = error("Unused")
    override fun itemsFlow(): Flow<List<EducationItemEntity>> = error("Unused")
    override suspend fun upsertInstitutions(institutions: List<InstitutionEntity>) = error("Unused")
    override suspend fun upsertLocations(locations: List<EducationLocationEntity>) = error("Unused")
    override suspend fun upsertItems(items: List<EducationItemEntity>) = error("Unused")
    override suspend fun clearInstitutions() = error("Unused")
    override suspend fun clearLocations() = error("Unused")
    override suspend fun clearItems() = error("Unused")
}

private fun EducationEntityGraph.toReadGraph(): EducationEntityReadGraph = EducationEntityReadGraph(
    institutions = institutions.map { institution ->
        InstitutionWithEducationEntity(
            institution = institution,
            location = locations.single { location -> location.id == institution.locationId },
            items = items.filter { item -> item.institutionId == institution.id },
        )
    },
)

private val educationResponse = EducationResponseDto(
    institutions = listOf(
        InstitutionDto(
            id = "ysu",
            name = "Yerevan State University",
            shortName = "YSU",
            type = "university",
            description = "Public university in Armenia.",
            websiteUrl = "https://www.ysu.am",
            location = EducationLocationDto(city = "Yerevan", country = "Armenia"),
        ),
    ),
    items = listOf(
        EducationItemDto(
            id = "ysu-management-master",
            institutionId = "ysu",
            program = EducationProgramDto(
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
