package com.gasparian.rob.feature.profile.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.profile.data.local.ProfileContactEntity
import com.gasparian.rob.feature.profile.data.local.ProfileDao
import com.gasparian.rob.feature.profile.data.local.ProfileEntity
import com.gasparian.rob.feature.profile.data.local.ProfileEntityGraph
import com.gasparian.rob.feature.profile.data.local.ProfileLocationEntity
import com.gasparian.rob.feature.profile.data.local.ProfileQualificationEntity
import com.gasparian.rob.feature.profile.data.mapper.toEntityGraph
import com.gasparian.rob.feature.profile.data.remote.ProfileContactDto
import com.gasparian.rob.feature.profile.data.remote.ProfileLocationDto
import com.gasparian.rob.feature.profile.data.remote.ProfileRemoteDataSource
import com.gasparian.rob.feature.profile.data.remote.ProfileResponseDto
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
        val remoteDataSource = mockk<ProfileRemoteDataSource>()
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
        val remoteDataSource = mockk<ProfileRemoteDataSource>()
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
        val remoteDataSource = mockk<ProfileRemoteDataSource>()
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
    initialGraph: ProfileEntityGraph? = null,
) : ProfileDao {
    private val graphFlow = MutableStateFlow(initialGraph)
    var lastReplacedGraph: ProfileEntityGraph? = null

    override fun profileGraphFlow(): Flow<ProfileEntityGraph?> = graphFlow

    override suspend fun replaceProfile(graph: ProfileEntityGraph) {
        lastReplacedGraph = graph
        graphFlow.value = graph
    }

    override suspend fun getProfileGraph(): ProfileEntityGraph? = graphFlow.value
    override suspend fun getProfile(): ProfileEntity? = error("Unused")
    override fun profileFlow(): Flow<ProfileEntity?> = error("Unused")
    override fun locationsFlow(): Flow<List<ProfileLocationEntity>> = error("Unused")
    override fun contactsFlow(): Flow<List<ProfileContactEntity>> = error("Unused")
    override fun qualificationsFlow(): Flow<List<ProfileQualificationEntity>> = error("Unused")
    override suspend fun getLocation(id: String): ProfileLocationEntity? = error("Unused")
    override suspend fun getContact(id: String): ProfileContactEntity? = error("Unused")
    override suspend fun getQualifications(profileId: String): List<ProfileQualificationEntity> = error("Unused")
    override suspend fun upsertProfile(profile: ProfileEntity) = error("Unused")
    override suspend fun upsertLocation(location: ProfileLocationEntity) = error("Unused")
    override suspend fun upsertContact(contact: ProfileContactEntity) = error("Unused")
    override suspend fun upsertQualifications(qualifications: List<ProfileQualificationEntity>) = error("Unused")
    override suspend fun clearProfile() = error("Unused")
    override suspend fun clearLocations() = error("Unused")
    override suspend fun clearContacts() = error("Unused")
    override suspend fun clearQualifications() = error("Unused")
}

private val profileResponse = ProfileResponseDto(
    id = "rob",
    displayName = "Robert Gasparyan",
    headline = "Android Engineer",
    shortBio = "Senior Android engineer.",
    location = ProfileLocationDto(city = "Toronto", region = "ON", country = "Canada"),
    contact = ProfileContactDto(
        email = "rob.gasparian@gmail.com",
        phone = "+1 437-551-9859",
        linkedin = "linkedin.com/in/rob-gasparian/",
    ),
    professionalProfile = "Professional profile",
    summaryOfQualifications = emptyList(),
)
