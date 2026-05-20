package com.gasparian.rob.feature.profile.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.profile.data.local.ProfileContactEntity
import com.gasparian.rob.feature.profile.data.local.ProfileEntity
import com.gasparian.rob.feature.profile.data.local.ProfileEntityGraph
import com.gasparian.rob.feature.profile.data.local.ProfileLocationEntity
import com.gasparian.rob.feature.profile.data.local.ProfileQualificationEntity
import com.gasparian.rob.feature.profile.data.remote.ProfileResponseDto
import com.gasparian.rob.feature.profile.domain.model.Profile
import com.gasparian.rob.feature.profile.domain.model.ProfileContact
import com.gasparian.rob.feature.profile.domain.model.ProfileLocation
import com.gasparian.rob.feature.profile.domain.model.ProfileQualification

internal fun ProfileResponseDto.toEntityGraph(
    updatedAtMillis: Long,
): ProfileEntityGraph {
    val locationId = "profile:$id"
    val contactId = "profile:$id"
    return ProfileEntityGraph(
        profile =
        ProfileEntity(
            id = id,
            displayName = displayName,
            headline = headline,
            shortBio = shortBio,
            locationId = locationId,
            contactId = contactId,
            professionalProfile = professionalProfile,
            updatedAtMillis = updatedAtMillis,
        ),
        location =
        ProfileLocationEntity(
            id = locationId,
            city = location.city,
            region = location.region,
            country = location.country,
            addressLine = location.addressLine,
        ),
        contact =
        ProfileContactEntity(
            id = contactId,
            email = contact.email,
            phone = contact.phone,
            linkedin = contact.linkedin,
        ),
        qualifications =
        summaryOfQualifications.mapIndexed { index, qualification ->
            ProfileQualificationEntity(
                id = "$id:qualification:$index",
                profileId = id,
                title = qualification.title,
                description = qualification.description,
                sortIndex = index,
            )
        },
    )
}

internal fun ProfileEntityGraph.toDomain(): Profile = Profile(
    id = profile.id,
    displayName = profile.displayName,
    headline = profile.headline,
    shortBio = profile.shortBio,
    location =
    ProfileLocation(
        city = location.city,
        region = location.region,
        country = location.country,
        addressLine = location.addressLine,
    ),
    contact =
    ProfileContact(
        email = contact.email,
        phone = contact.phone,
        linkedin = contact.linkedin,
    ),
    professionalProfile = profile.professionalProfile,
    summaryOfQualifications =
    qualifications.map { qualification ->
        ProfileQualification(
            title = qualification.title,
            description = qualification.description,
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())
