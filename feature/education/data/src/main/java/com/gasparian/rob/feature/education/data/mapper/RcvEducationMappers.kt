package com.gasparian.rob.feature.education.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.feature.education.data.local.EducationEntityGraph
import com.gasparian.rob.feature.education.data.local.EducationItemEntity
import com.gasparian.rob.feature.education.data.local.EducationLocationEntity
import com.gasparian.rob.feature.education.data.local.InstitutionEntity
import com.gasparian.rob.feature.education.data.remote.EducationResponseDto
import com.gasparian.rob.feature.education.domain.model.Education
import com.gasparian.rob.feature.education.domain.model.EducationItem
import com.gasparian.rob.feature.education.domain.model.EducationLocation
import com.gasparian.rob.feature.education.domain.model.EducationProgram
import com.gasparian.rob.feature.education.domain.model.EducationStatus
import com.gasparian.rob.feature.education.domain.model.Faculty
import com.gasparian.rob.feature.education.domain.model.Institution
import com.gasparian.rob.feature.education.domain.model.InstitutionType
import kotlinx.datetime.LocalDate

internal fun EducationResponseDto.toEntityGraph(): EducationEntityGraph = EducationEntityGraph(
    institutions =
    institutions.map { institution ->
        InstitutionEntity(
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
        EducationLocationEntity(
            id = "institution:${institution.id}",
            city = institution.location.city,
            region = institution.location.region,
            country = institution.location.country,
            addressLine = institution.location.addressLine,
        )
    },
    items =
    items.map { item ->
        EducationItemEntity(
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

internal fun EducationEntityGraph.toDomain(): Education = Education(
    institutions =
    institutions.map { institution ->
        val location =
            requireNotNull(locations.firstOrNull { location -> location.id == institution.locationId })
        Institution(
            id = institution.id,
            name = institution.name,
            shortName = institution.shortName,
            type = institution.type.toInstitutionType(),
            description = institution.description,
            websiteUrl = institution.websiteUrl,
            location =
            EducationLocation(
                city = location.city,
                region = location.region,
                country = location.country,
                addressLine = location.addressLine,
            ),
        )
    },
    items =
    items.map { item ->
        EducationItem(
            id = item.id,
            institutionId = item.institutionId,
            faculty = item.facultyName?.let(::Faculty),
            program =
            EducationProgram(
                name = item.programName,
                credential = item.credential,
                fieldOfStudy = item.fieldOfStudy,
            ),
            startDate = LocalDate.parse(item.startDate),
            endDate = LocalDate.parse(item.endDate),
            status = item.status.toEducationStatus(),
        )
    },
)

internal fun RcvNetworkError.toException(): Throwable = IllegalStateException(toString())

private fun String.toInstitutionType(): InstitutionType = when (lowercase()) {
    "public university", "university" -> InstitutionType.PublicUniversity
    "training center", "bootcamp" -> InstitutionType.TrainingCenter
    else -> InstitutionType.Unknown(rawValue = this)
}

private fun String.toEducationStatus(): EducationStatus = when (lowercase()) {
    "completed" -> EducationStatus.Completed
    "in_progress", "in progress", "active" -> EducationStatus.InProgress
    else -> EducationStatus.Unknown(rawValue = this)
}
