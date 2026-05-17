package com.gasparian.rob.feature.home.data.mapper

import com.gasparian.rob.core.network.RcvNetworkError
import com.gasparian.rob.core.network.RcvNetworkResult
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeContactDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeCurrentFocusDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeEducationItemDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeEducationProgramDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeEducationResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeExperienceResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeExperienceRoleDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeFacultyDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeInstitutionDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeLocationDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeMilestoneDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeMilestonesResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeProfileResponseDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeQualificationDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeSkillCategoryDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeSkillDto
import com.gasparian.rob.feature.home.data.remote.dto.RcvHomeSkillsResponseDto
import com.gasparian.rob.feature.home.domain.model.RcvHomeContact
import com.gasparian.rob.feature.home.domain.model.RcvHomeCurrentFocus
import com.gasparian.rob.feature.home.domain.model.RcvHomeEducation
import com.gasparian.rob.feature.home.domain.model.RcvHomeEducationItem
import com.gasparian.rob.feature.home.domain.model.RcvHomeEducationProgram
import com.gasparian.rob.feature.home.domain.model.RcvHomeError
import com.gasparian.rob.feature.home.domain.model.RcvHomeExperience
import com.gasparian.rob.feature.home.domain.model.RcvHomeExperienceRole
import com.gasparian.rob.feature.home.domain.model.RcvHomeFaculty
import com.gasparian.rob.feature.home.domain.model.RcvHomeInstitution
import com.gasparian.rob.feature.home.domain.model.RcvHomeLocation
import com.gasparian.rob.feature.home.domain.model.RcvHomeMilestone
import com.gasparian.rob.feature.home.domain.model.RcvHomeMilestones
import com.gasparian.rob.feature.home.domain.model.RcvHomeProfile
import com.gasparian.rob.feature.home.domain.model.RcvHomeQualification
import com.gasparian.rob.feature.home.domain.model.RcvHomeResult
import com.gasparian.rob.feature.home.domain.model.RcvHomeSkill
import com.gasparian.rob.feature.home.domain.model.RcvHomeSkillCategory
import com.gasparian.rob.feature.home.domain.model.RcvHomeSkills

internal fun RcvHomeProfileResponseDto.toDomain(): RcvHomeProfile = RcvHomeProfile(
    id = id,
    displayName = displayName,
    headline = headline,
    shortBio = shortBio,
    location = location.toDomain(),
    contact = contact.toDomain(),
    professionalProfile = professionalProfile,
    summaryOfQualifications = summaryOfQualifications.map(RcvHomeQualificationDto::toDomain),
)

internal fun RcvHomeSkillsResponseDto.toDomain(): RcvHomeSkills = RcvHomeSkills(
    categories = categories.map(RcvHomeSkillCategoryDto::toDomain),
    skills = skills.map(RcvHomeSkillDto::toDomain),
)

internal fun RcvHomeExperienceResponseDto.toDomain(): RcvHomeExperience = RcvHomeExperience(
    roles = roles.map(RcvHomeExperienceRoleDto::toDomain),
)

internal fun RcvHomeEducationResponseDto.toDomain(): RcvHomeEducation = RcvHomeEducation(
    institutions = institutions.map(RcvHomeInstitutionDto::toDomain),
    items = items.map(RcvHomeEducationItemDto::toDomain),
)

internal fun RcvHomeMilestonesResponseDto.toDomain(): RcvHomeMilestones = RcvHomeMilestones(
    currentFocus = currentFocus.toDomain(),
    recentMilestones = recentMilestones.map(RcvHomeMilestoneDto::toDomain),
)

internal fun RcvNetworkResult.Failure.toDomain(): RcvHomeResult.Failure = RcvHomeResult.Failure(error = error.toDomain())

private fun RcvNetworkError.toDomain(): RcvHomeError = when (this) {
    is RcvNetworkError.Http ->
        RcvHomeError.Http(
            code = code,
            message = message,
        )

    RcvNetworkError.NetworkUnavailable -> RcvHomeError.NetworkUnavailable

    RcvNetworkError.Serialization -> RcvHomeError.Serialization

    RcvNetworkError.Timeout -> RcvHomeError.Timeout

    is RcvNetworkError.Unknown -> RcvHomeError.Unknown(message)
}

private fun RcvHomeLocationDto.toDomain(): RcvHomeLocation = RcvHomeLocation(
    city = city,
    region = region,
    country = country,
    addressLine = addressLine,
)

private fun RcvHomeContactDto.toDomain(): RcvHomeContact = RcvHomeContact(
    email = email,
    phone = phone,
    linkedin = linkedin,
)

private fun RcvHomeQualificationDto.toDomain(): RcvHomeQualification = RcvHomeQualification(
    title = title,
    description = description,
)

private fun RcvHomeSkillCategoryDto.toDomain(): RcvHomeSkillCategory = RcvHomeSkillCategory(
    id = id,
    name = name,
)

private fun RcvHomeSkillDto.toDomain(): RcvHomeSkill = RcvHomeSkill(
    id = id,
    name = name,
    shortName = shortName,
    categoryId = categoryId,
    proficiencyType = proficiencyType,
    level = level,
    yearsOfExperience = yearsOfExperience,
    isCore = isCore,
    contexts = contexts,
)

private fun RcvHomeExperienceRoleDto.toDomain(): RcvHomeExperienceRole = RcvHomeExperienceRole(
    title = title,
    company = company,
    startDate = startDate,
    endDate = endDate,
    location = location,
    workArrangement = workArrangement,
    summary = summary,
    highlights = highlights,
)

private fun RcvHomeInstitutionDto.toDomain(): RcvHomeInstitution = RcvHomeInstitution(
    id = id,
    name = name,
    shortName = shortName,
    type = type,
    description = description,
    websiteUrl = websiteUrl,
    location = location.toDomain(),
)

private fun RcvHomeEducationItemDto.toDomain(): RcvHomeEducationItem = RcvHomeEducationItem(
    id = id,
    institutionId = institutionId,
    faculty = faculty?.toDomain(),
    program = program.toDomain(),
    startDate = startDate,
    endDate = endDate,
    status = status,
)

private fun RcvHomeFacultyDto.toDomain(): RcvHomeFaculty = RcvHomeFaculty(
    name = name,
)

private fun RcvHomeEducationProgramDto.toDomain(): RcvHomeEducationProgram = RcvHomeEducationProgram(
    name = name,
    credential = credential,
    fieldOfStudy = fieldOfStudy,
)

private fun RcvHomeCurrentFocusDto.toDomain(): RcvHomeCurrentFocus = RcvHomeCurrentFocus(
    summary = summary,
    topics = topics,
)

private fun RcvHomeMilestoneDto.toDomain(): RcvHomeMilestone = RcvHomeMilestone(
    id = id,
    title = title,
    description = description,
    completedAt = completedAt,
    topics = topics,
)
