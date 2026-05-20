package com.gasparian.rob.feature.experience.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ExperienceEntityGraph(
    val roles: List<ExperienceRoleEntity>,
    val highlights: List<ExperienceHighlightEntity>,
) {
    fun isEmpty(): Boolean = roles.isEmpty()
}

@Entity(tableName = "rcv_experience_role")
data class ExperienceRoleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val company: String,
    val startDate: String,
    val endDate: String?,
    val location: String,
    val workArrangement: String,
    val summary: String,
)

@Entity(tableName = "rcv_experience_highlight")
data class ExperienceHighlightEntity(
    @PrimaryKey val id: String,
    val roleId: String,
    val text: String,
    val sortIndex: Int,
)
