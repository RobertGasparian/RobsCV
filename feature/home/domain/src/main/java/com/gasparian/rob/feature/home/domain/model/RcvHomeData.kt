package com.gasparian.rob.feature.home.domain.model

data class RcvHomeData(
    val profile: RcvHomeProfile,
    val skills: RcvHomeSkills,
    val experience: RcvHomeExperience,
    val education: RcvHomeEducation,
    val milestones: RcvHomeMilestones,
)

data class RcvHomeProfile(
    val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val location: RcvHomeLocation,
    val contact: RcvHomeContact,
    val professionalProfile: String,
    val summaryOfQualifications: List<RcvHomeQualification>,
)

data class RcvHomeLocation(
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

data class RcvHomeContact(
    val email: String,
    val phone: String,
    val linkedin: String,
)

data class RcvHomeQualification(
    val title: String,
    val description: String,
)

data class RcvHomeSkills(
    val categories: List<RcvHomeSkillCategory>,
    val skills: List<RcvHomeSkill>,
)

data class RcvHomeSkillCategory(
    val id: String,
    val name: String,
)

data class RcvHomeSkill(
    val id: String,
    val name: String,
    val shortName: String?,
    val categoryId: String,
    val proficiencyType: String,
    val level: String?,
    val yearsOfExperience: Int?,
    val isCore: Boolean,
    val contexts: List<String>,
)

data class RcvHomeExperience(
    val roles: List<RcvHomeExperienceRole>,
)

data class RcvHomeExperienceRole(
    val title: String,
    val company: String,
    val startDate: String,
    val endDate: String?,
    val location: String,
    val workArrangement: String,
    val summary: String,
    val highlights: List<String>,
)

data class RcvHomeEducation(
    val institutions: List<RcvHomeInstitution>,
    val items: List<RcvHomeEducationItem>,
)

data class RcvHomeInstitution(
    val id: String,
    val name: String,
    val shortName: String?,
    val type: String,
    val description: String,
    val websiteUrl: String,
    val location: RcvHomeLocation,
)

data class RcvHomeEducationItem(
    val id: String,
    val institutionId: String,
    val faculty: RcvHomeFaculty?,
    val program: RcvHomeEducationProgram,
    val startDate: String,
    val endDate: String,
    val status: String,
)

data class RcvHomeFaculty(
    val name: String,
)

data class RcvHomeEducationProgram(
    val name: String,
    val credential: String,
    val fieldOfStudy: String,
)

data class RcvHomeMilestones(
    val currentFocus: RcvHomeCurrentFocus,
    val recentMilestones: List<RcvHomeMilestone>,
)

data class RcvHomeCurrentFocus(
    val summary: String,
    val topics: List<String>,
)

data class RcvHomeMilestone(
    val id: String,
    val title: String,
    val description: String,
    val completedAt: String,
    val topics: List<String>,
)
