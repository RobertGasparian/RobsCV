package com.gasparian.rob.feature.skills.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RcvSkillsResponseDto(
    val categories: List<RcvSkillCategoryDto>,
    val skills: List<RcvSkillDto>,
)

@Serializable
data class RcvSkillCategoryDto(
    val id: String,
    val name: String,
)

@Serializable
data class RcvSkillDto(
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
