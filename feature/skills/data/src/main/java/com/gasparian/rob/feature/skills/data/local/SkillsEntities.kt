package com.gasparian.rob.feature.skills.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class SkillsEntityGraph(
    val categories: List<SkillCategoryEntity>,
    val skills: List<SkillEntity>,
    val contexts: List<SkillContextEntity>,
) {
    fun isEmpty(): Boolean = categories.isEmpty() && skills.isEmpty()
}

@Entity(tableName = "rcv_skill_category")
data class SkillCategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
)

@Entity(tableName = "rcv_skill")
data class SkillEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortName: String?,
    val categoryId: String,
    val proficiencyType: String,
    val level: String?,
    val yearsOfExperience: Int?,
    val isCore: Boolean,
)

@Entity(
    tableName = "rcv_skill_context",
    primaryKeys = ["skillId", "context"],
)
data class SkillContextEntity(
    val skillId: String,
    val context: String,
    val sortIndex: Int,
)
