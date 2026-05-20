package com.gasparian.rob.feature.skills.presentation

data class SkillsUiState(
    val isLoading: Boolean,
    val skills: SkillsUiModel?,
    val errorMessage: String?,
) {
    companion object {
        fun initialState() = SkillsUiState(
            isLoading = true,
            skills = null,
            errorMessage = null,
        )

        fun preview() = SkillsUiState(
            isLoading = false,
            skills =
            SkillsUiModel(
                categories =
                listOf(
                    SkillCategoryUiModel(
                        id = "android",
                        name = "Android",
                    ),
                ),
                skills =
                listOf(
                    SkillUiModel(
                        id = "kotlin",
                        name = "Kotlin",
                        shortName = null,
                        categoryId = "android",
                        proficiencyType = SkillProficiencyTypeUiModel.LEVELED,
                        level = SkillLevelUiModel.EXPERT,
                        yearsOfExperience = 8,
                        isCore = true,
                        contexts = listOf("Android", "KMP"),
                    ),
                ),
            ),
            errorMessage = null,
        )
    }
}

data class SkillsUiModel(
    val categories: List<SkillCategoryUiModel>,
    val skills: List<SkillUiModel>,
)

data class SkillCategoryUiModel(
    val id: String,
    val name: String,
)

data class SkillUiModel(
    val id: String,
    val name: String,
    val shortName: String?,
    val categoryId: String,
    val proficiencyType: SkillProficiencyTypeUiModel,
    val level: SkillLevelUiModel?,
    val yearsOfExperience: Int?,
    val isCore: Boolean,
    val contexts: List<String>,
)

enum class SkillProficiencyTypeUiModel {
    LEVELED,
    USED,
    UNKNOWN,
}

enum class SkillLevelUiModel {
    EXPERT,
    ADVANCED,
    INTERMEDIATE,
    FAMILIAR,
    UNKNOWN,
}
