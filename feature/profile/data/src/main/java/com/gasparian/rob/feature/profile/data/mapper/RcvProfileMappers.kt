package com.gasparian.rob.feature.profile.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.profile.data.local.RcvProfileContactEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileEntityGraph
import com.gasparian.rob.feature.profile.data.local.RcvProfileLocationEntity
import com.gasparian.rob.feature.profile.data.local.RcvProfileQualificationEntity
import com.gasparian.rob.feature.profile.data.remote.RcvProfileResponseDto
import com.gasparian.rob.feature.profile.domain.model.RcvProfile
import com.gasparian.rob.feature.profile.domain.model.RcvProfileContact
import com.gasparian.rob.feature.profile.domain.model.RcvProfileLocation
import com.gasparian.rob.feature.profile.domain.model.RcvProfileQualification

internal fun RcvProfileResponseDto.toEntityGraph(
    updatedAtMillis: Long,
): RcvProfileEntityGraph {
    val locationId = "profile:$id"
    val contactId = "profile:$id"
    return RcvProfileEntityGraph(
        profile =
        RcvProfileEntity(
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
        RcvProfileLocationEntity(
            id = locationId,
            city = location.city,
            region = location.region,
            country = location.country,
            addressLine = location.addressLine,
        ),
        contact =
        RcvProfileContactEntity(
            id = contactId,
            email = contact.email,
            phone = contact.phone,
            linkedin = contact.linkedin,
        ),
        qualifications =
        summaryOfQualifications.mapIndexed { index, qualification ->
            RcvProfileQualificationEntity(
                id = "$id:qualification:$index",
                profileId = id,
                title = qualification.title,
                description = qualification.description,
                sortIndex = index,
            )
        },
    )
}

internal fun RcvProfileEntityGraph.toDomain(): RcvProfile = RcvProfile(
    id = profile.id,
    displayName = profile.displayName,
    headline = profile.headline,
    shortBio = profile.shortBio,
    location =
    RcvProfileLocation(
        city = location.city,
        region = location.region,
        country = location.country,
        addressLine = location.addressLine,
    ),
    contact =
    RcvProfileContact(
        email = contact.email,
        phone = contact.phone,
        linkedin = contact.linkedin,
    ),
    professionalProfile = profile.professionalProfile,
    summaryOfQualifications =
    qualifications.map { qualification ->
        RcvProfileQualification(
            title = qualification.title,
            description = qualification.description,
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())
