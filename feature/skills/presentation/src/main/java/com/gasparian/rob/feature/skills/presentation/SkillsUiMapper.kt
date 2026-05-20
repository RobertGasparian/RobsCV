package com.gasparian.rob.feature.skills.presentation

import com.gasparian.rob.feature.skills.domain.model.Skill
import com.gasparian.rob.feature.skills.domain.model.SkillCategory
import com.gasparian.rob.feature.skills.domain.model.SkillLevel
import com.gasparian.rob.feature.skills.domain.model.SkillProficiencyType
import com.gasparian.rob.feature.skills.domain.model.Skills

internal fun Result<Skills>.toSkillsUiState(): SkillsUiState = fold(
    onSuccess = { skills ->
        SkillsUiState(
            isLoading = false,
            skills = skills.toUiModel(),
            errorMessage = null,
        )
    },
    onFailure = { throwable ->
        SkillsUiState(
            isLoading = false,
            skills = null,
            errorMessage = throwable.toUiErrorMessage(),
        )
    },
)

private fun Skills.toUiModel() = SkillsUiModel(
    categories = categories.map(SkillCategory::toUiModel),
    skills = skills.map(Skill::toUiModel),
)

private fun SkillCategory.toUiModel() = SkillCategoryUiModel(
    id = id,
    name = name,
)

private fun Skill.toUiModel() = SkillUiModel(
    id = id,
    name = name,
    shortName = shortName,
    categoryId = categoryId,
    proficiencyType = proficiencyType.toUiModel(),
    level = level?.toUiModel(),
    yearsOfExperience = yearsOfExperience,
    isCore = isCore,
    contexts = contexts,
)

private fun SkillProficiencyType.toUiModel() = when (this) {
    SkillProficiencyType.LEVELED -> SkillProficiencyTypeUiModel.LEVELED
    SkillProficiencyType.USED -> SkillProficiencyTypeUiModel.USED
    SkillProficiencyType.UNKNOWN -> SkillProficiencyTypeUiModel.UNKNOWN
}

private fun SkillLevel.toUiModel() = when (this) {
    SkillLevel.EXPERT -> SkillLevelUiModel.EXPERT
    SkillLevel.ADVANCED -> SkillLevelUiModel.ADVANCED
    SkillLevel.INTERMEDIATE -> SkillLevelUiModel.INTERMEDIATE
    SkillLevel.FAMILIAR -> SkillLevelUiModel.FAMILIAR
    SkillLevel.UNKNOWN -> SkillLevelUiModel.UNKNOWN
}

private fun Throwable.toUiErrorMessage(): String = message ?: "Unable to load skills."
