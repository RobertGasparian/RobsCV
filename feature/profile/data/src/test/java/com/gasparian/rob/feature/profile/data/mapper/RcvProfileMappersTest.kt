package com.gasparian.rob.feature.profile.data.mapper

import com.gasparian.rob.feature.profile.data.remote.RcvProfileContactDto
import com.gasparian.rob.feature.profile.data.remote.RcvProfileLocationDto
import com.gasparian.rob.feature.profile.data.remote.RcvProfileQualificationDto
import com.gasparian.rob.feature.profile.data.remote.RcvProfileResponseDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RcvProfileMappersTest {
    @Test
    fun `toEntityGraph creates stable relational profile graph`() {
        val graph = profileDto.toEntityGraph(updatedAtMillis = 123L)

        assertEquals("rob", graph.profile.id)
        assertEquals("profile:rob", graph.profile.locationId)
        assertEquals("profile:rob", graph.location.id)
        assertEquals("profile:rob", graph.profile.contactId)
        assertEquals("profile:rob", graph.contact.id)
        assertEquals(123L, graph.profile.updatedAtMillis)
        assertEquals("rob:qualification:0", graph.qualifications.first().id)
        assertEquals(0, graph.qualifications.first().sortIndex)
    }

    @Test
    fun `toDomain maps entity graph to profile domain model`() {
        val domain = profileDto.toEntityGraph(updatedAtMillis = 123L).toDomain()

        assertEquals("Robert Gasparyan", domain.displayName)
        assertEquals("Toronto", domain.location.city)
        assertEquals("rob.gasparian@gmail.com", domain.contact.email)
        assertEquals("Core Expertise", domain.summaryOfQualifications.single().title)
    }
}

private val profileDto = RcvProfileResponseDto(
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
    summaryOfQualifications = listOf(
        RcvProfileQualificationDto(
            title = "Core Expertise",
            description = "Android development.",
        ),
    ),
)
