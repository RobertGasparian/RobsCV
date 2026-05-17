package com.gasparian.rob.feature.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RcvHomeSkillsResponseDto(
    val categories: List<RcvHomeSkillCategoryDto>,
    val skills: List<RcvHomeSkillDto>,
)

@Serializable
data class RcvHomeSkillCategoryDto(
    val id: String,
    val name: String,
)

@Serializable
data class RcvHomeSkillDto(
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
