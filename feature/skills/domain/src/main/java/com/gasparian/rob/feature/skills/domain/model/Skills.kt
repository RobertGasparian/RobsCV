package com.gasparian.rob.feature.skills.domain.model

data class Skills(
    val categories: List<SkillCategory>,
    val skills: List<Skill>,
)

data class SkillCategory(
    val id: String,
    val name: String,
)

data class Skill(
    val id: String,
    val name: String,
    val shortName: String?,
    val categoryId: String,
    val proficiencyType: SkillProficiencyType,
    val level: SkillLevel?,
    val yearsOfExperience: Int?,
    val isCore: Boolean,
    val contexts: List<String>,
)

enum class SkillProficiencyType {
    LEVELED,
    USED,
    UNKNOWN,
}

enum class SkillLevel {
    EXPERT,
    ADVANCED,
    INTERMEDIATE,
    FAMILIAR,
    UNKNOWN,
}
