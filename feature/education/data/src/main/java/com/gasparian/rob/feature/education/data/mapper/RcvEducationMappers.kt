package com.gasparian.rob.feature.education.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.education.data.local.RcvEducationEntityGraph
import com.gasparian.rob.feature.education.data.local.RcvEducationItemEntity
import com.gasparian.rob.feature.education.data.local.RcvEducationLocationEntity
import com.gasparian.rob.feature.education.data.local.RcvInstitutionEntity
import com.gasparian.rob.feature.education.data.remote.RcvEducationResponseDto
import com.gasparian.rob.feature.education.domain.model.RcvEducation
import com.gasparian.rob.feature.education.domain.model.RcvEducationItem
import com.gasparian.rob.feature.education.domain.model.RcvEducationLocation
import com.gasparian.rob.feature.education.domain.model.RcvEducationProgram
import com.gasparian.rob.feature.education.domain.model.RcvFaculty
import com.gasparian.rob.feature.education.domain.model.RcvInstitution

internal fun RcvEducationResponseDto.toEntityGraph(): RcvEducationEntityGraph = RcvEducationEntityGraph(
    institutions =
    institutions.map { institution ->
        RcvInstitutionEntity(
            id = institution.id,
            name = institution.name,
            shortName = institution.shortName,
            type = institution.type,
            description = institution.description,
            websiteUrl = institution.websiteUrl,
            locationId = "institution:${institution.id}",
        )
    },
    locations =
    institutions.map { institution ->
        RcvEducationLocationEntity(
            id = "institution:${institution.id}",
            city = institution.location.city,
            region = institution.location.region,
            country = institution.location.country,
            addressLine = institution.location.addressLine,
        )
    },
    items =
    items.map { item ->
        RcvEducationItemEntity(
            id = item.id,
            institutionId = item.institutionId,
            facultyName = item.faculty?.name,
            programName = item.program.name,
            credential = item.program.credential,
            fieldOfStudy = item.program.fieldOfStudy,
            startDate = item.startDate,
            endDate = item.endDate,
            status = item.status,
        )
    },
)

internal fun RcvEducationEntityGraph.toDomain(): RcvEducation = RcvEducation(
    institutions =
    institutions.map { institution ->
        val location =
            requireNotNull(locations.firstOrNull { location -> location.id == institution.locationId })
        RcvInstitution(
            id = institution.id,
            name = institution.name,
            shortName = institution.shortName,
            type = institution.type,
            description = institution.description,
            websiteUrl = institution.websiteUrl,
            location =
            RcvEducationLocation(
                city = location.city,
                region = location.region,
                country = location.country,
                addressLine = location.addressLine,
            ),
        )
    },
    items =
    items.map { item ->
        RcvEducationItem(
            id = item.id,
            institutionId = item.institutionId,
            faculty = item.facultyName?.let(::RcvFaculty),
            program =
            RcvEducationProgram(
                name = item.programName,
                credential = item.credential,
                fieldOfStudy = item.fieldOfStudy,
            ),
            startDate = item.startDate,
            endDate = item.endDate,
            status = item.status,
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())
