package com.gasparian.rob.feature.home.data.repository

import app.cash.turbine.test
import com.gasparian.rob.core.network.RcvNetworkClient
import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResponse
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.home.data.remote.RcvHomeEndpoints
import com.gasparian.rob.feature.home.data.remote.RcvHomeRemoteDataSource
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeContactDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeCurrentFocusDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeEducationResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeExperienceResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeLocationDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeMilestonesResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeProfileResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeSkillsResponseDto
import com.gasparian.rob.feature.home.domain.model.RcvHomeError
import com.gasparian.rob.feature.home.domain.model.RcvHomeResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkOnlyRcvHomeRepositoryTest {
    @Test
    fun `observeHomeData emits combined network data`() = runTest {
        val repository =
            NetworkOnlyRcvHomeRepository(
                remoteDataSource =
                RcvHomeRemoteDataSource(
                    networkClient =
                    FakeRcvNetworkClient(
                        responses =
                        mapOf(
                            RcvHomeEndpoints.PROFILE to RcvNetworkResult.Success(profileDto),
                            RcvHomeEndpoints.SKILLS to RcvNetworkResult.Success(skillsDto),
                            RcvHomeEndpoints.EXPERIENCE to RcvNetworkResult.Success(experienceDto),
                            RcvHomeEndpoints.EDUCATION to RcvNetworkResult.Success(educationDto),
                            RcvHomeEndpoints.MILESTONES to RcvNetworkResult.Success(milestonesDto),
                        ),
                    ),
                ),
            )

        repository.observeHomeData().test {
            val result = awaitItem()

            check(result is RcvHomeResult.Success)
            assertEquals("Robert Gasparyan", result.data.profile.displayName)
            assertEquals("Modern Android", result.data.milestones.currentFocus.summary)
            awaitComplete()
        }
    }

    @Test
    fun `observeHomeData emits first network failure`() = runTest {
        val expectedError = RcvNetworkError.Http(code = 500, message = "Server Error")
        val repository =
            NetworkOnlyRcvHomeRepository(
                remoteDataSource =
                RcvHomeRemoteDataSource(
                    networkClient =
                    FakeRcvNetworkClient(
                        responses =
                        mapOf(
                            RcvHomeEndpoints.PROFILE to RcvNetworkResult.Success(profileDto),
                            RcvHomeEndpoints.SKILLS to RcvNetworkResult.Failure(expectedError),
                        ),
                    ),
                ),
            )

        repository.observeHomeData().test {
            val result = awaitItem()

            check(result is RcvHomeResult.Failure)
            assertEquals(
                RcvHomeError.Http(
                    code = expectedError.code,
                    message = expectedError.message,
                ),
                result.error,
            )
            awaitComplete()
        }
    }
}

private class FakeRcvNetworkClient(
    private val responses: Map<String, RcvNetworkResult<*>>,
) : RcvNetworkClient {
    override suspend fun <T> get(
        path: String,
        responseMapper: suspend RcvNetworkResponse.() -> T,
    ): RcvNetworkResult<T> {
        @Suppress("UNCHECKED_CAST")
        return responses[path] as? RcvNetworkResult<T>
            ?: error("No fake response for $path")
    }
}

private val profileDto =
    RcvHomeProfileResponseDto(
        id = "robert-gasparyan",
        displayName = "Robert Gasparyan",
        headline = "Android Engineer",
        shortBio = "Senior Android Engineer.",
        location =
        RcvHomeLocationDto(
            city = "Toronto",
            region = "ON",
            country = "Canada",
        ),
        contact =
        RcvHomeContactDto(
            email = "rob.gasparian@gmail.com",
            phone = "+1 437-551-9859",
            linkedin = "https://linkedin.com/in/rob-gasparian/",
        ),
        professionalProfile = "Profile.",
        summaryOfQualifications = emptyList(),
    )

private val skillsDto =
    RcvHomeSkillsResponseDto(
        categories = emptyList(),
        skills = emptyList(),
    )

private val experienceDto =
    RcvHomeExperienceResponseDto(
        roles = emptyList(),
    )

private val educationDto =
    RcvHomeEducationResponseDto(
        institutions = emptyList(),
        items = emptyList(),
    )

private val milestonesDto =
    RcvHomeMilestonesResponseDto(
        currentFocus =
        RcvHomeCurrentFocusDto(
            summary = "Modern Android",
            topics = emptyList(),
        ),
        recentMilestones = emptyList(),
    )
