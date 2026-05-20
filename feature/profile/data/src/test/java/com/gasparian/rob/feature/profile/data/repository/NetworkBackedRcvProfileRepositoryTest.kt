package com.gasparian.rob.feature.profile.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.profile.data.local.RcvProfileContactEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileDao
import com.gasparian.rob.feature.profile.data.local.RcvProfileEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileEntityGraph
import com.gasparian.rob.feature.profile.data.local.RcvProfileLocationEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileQualificationEntity
import com.gasparian.rob.feature.profile.data.mapper.toEntityGraph
import com.gasparian.rob.feature.profile.data.remote.RcvProfileContactDto
import com.gasparian.rob.feature.profile.data.remote.RcvProfileLocationDto
import com.gasparian.rob.feature.profile.data.remote.RcvProfileRemoteDataSource
import com.gasparian.rob.feature.profile.data.remote.RcvProfileResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkBackedRcvProfileRepositoryTest {
    @Test
    fun `profile syncs remote data into dao before emitting`() = runTest {
        val dao = FakeRcvProfileDao()
        val remoteDataSource = mockk<RcvProfileRemoteDataSource>()
        coEvery { remoteDataSource.getProfile() } returns RcvNetworkResult.Success(profileResponse)
        val repository = NetworkBackedRcvProfileRepository(
            remoteDataSource = remoteDataSource,
            profileDao = dao,
            currentTimeMillis = { 123L },
        )

        repository.profile.test {
            val item = awaitItem()

            assertEquals("Robert Gasparyan", item.getOrThrow().displayName)
            assertEquals(123L, dao.lastReplacedGraph?.profile?.updatedAtMillis)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `profile emits cached data when remote sync fails`() = runTest {
        val dao = FakeRcvProfileDao(initialGraph = profileResponse.toEntityGraph(updatedAtMillis = 111L))
        val remoteDataSource = mockk<RcvProfileRemoteDataSource>()
        coEvery { remoteDataSource.getProfile() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvProfileRepository(remoteDataSource = remoteDataSource, profileDao = dao)

        repository.profile.test {
            val item = awaitItem()

            assertEquals("Robert Gasparyan", item.getOrThrow().displayName)
            assertEquals(null, dao.lastReplacedGraph)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `profile emits failure when remote sync fails and cache is empty`() = runTest {
        val dao = FakeRcvProfileDao()
        val remoteDataSource = mockk<RcvProfileRemoteDataSource>()
        coEvery { remoteDataSource.getProfile() } returns RcvNetworkResult.Failure(RcvNetworkError.Timeout)
        val repository = NetworkBackedRcvProfileRepository(remoteDataSource = remoteDataSource, profileDao = dao)

        repository.profile.test {
            val item = awaitItem()

            assertEquals(true, item.isFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakeRcvProfileDao(
    initialGraph: RcvProfileEntityGraph? = null,
) : RcvProfileDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: RcvProfileEntityGraph? = null

    override fun profileGraphFlow(): Flow<RcvProfileEntityGraph?> = graphFlow

    override suspend fun replaceProfile(graph: RcvProfileEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getProfileGraph(): RcvProfileEntityGraph? = graphFlow.value
    override suspend fun getProfile(): RcvProfileEntity? = error("Unused")
    override fun profileFlow(): Flow<RcvProfileEntity?> = error("Unused")
    override fun locationsFlow(): Flow<List<RcvProfileLocationEntity>> = error("Unused")
    override fun contactsFlow(): Flow<List<RcvProfileContactEntity>> = error("Unused")
    override fun qualificationsFlow(): Flow<List<RcvProfileQualificationEntity>> = error("Unused")
    override suspend fun getLocation(id: String): RcvProfileLocationEntity? = error("Unused")
    override suspend fun getContact(id: String): RcvProfileContactEntity? = error("Unused")
    override suspend fun getQualifications(profileId: String): List<RcvProfileQualificationEntity> = error("Unused")
    override suspend fun upsertProfile(profile: RcvProfileEntity) = error("Unused")
    override suspend fun upsertLocation(location: RcvProfileLocationEntity) = error("Unused")
    override suspend fun upsertContact(contact: RcvProfileContactEntity) = error("Unused")
    override suspend fun upsertQualifications(qualifications: List<RcvProfileQualificationEntity>) = error("Unused")
    override suspend fun clearProfile() = error("Unused")
    override suspend fun clearLocations() = error("Unused")
    override suspend fun clearContacts() = error("Unused")
    override suspend fun clearQualifications() = error("Unused")
}

private val profileResponse = RcvProfileResponseDto(
    id = "rob",
    displayName = "Robert Gasparyan",
    headline = "Android Engineer",
    shortBio = "Senior Android engineer.",
    location = RcvProfileLocationDto(city = "Toronto", region = "ON", country = "Canada"),
    contact = RcvProfileContactDto(
        email = "rob.gasparian@gmail.com",
        phone = "+1 437-551-9859",
        linkedin = "linkedin.com/in/rob-gasparian/",
    ),
    professionalProfile = "Professional profile",
    summaryOfQualifications = emptyList(),
)
