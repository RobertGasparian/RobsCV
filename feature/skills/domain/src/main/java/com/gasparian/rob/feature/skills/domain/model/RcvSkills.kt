package com.gasparian.rob.feature.skills.domain.model

data class RcvSkills(
    val categories: List<RcvSkillCategory>,
    val skills: List<RcvSkill>,
)

data class RcvSkillCategory(
    val id: String,
    val name: String,
)

data class RcvSkill(
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
