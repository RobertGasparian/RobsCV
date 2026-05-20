package com.gasparian.rob.feature.skills.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SkillsResponseDto(
    val categories: List<SkillCategoryDto>,
    val skills: List<SkillDto>,
)

@Serializable
data class SkillCategoryDto(
    val id: String,
    val name: String,
)

@Serializable
data class SkillDto(
    val id: String,
    val name: String,
    val shortName: String? = null,
    val categoryId: String,
    val proficiencyType: String,
    val level: String? = null,
    val yearsOfExperience: Int? = null,
    val isCore: Boolean,
    val contexts: List<String>,
)
