package com.gasparian.rob.feature.experience.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class RcvExperienceEntityGraph(
    val roles: List<RcvExperienceRoleEntity>,
    val highlights: List<RcvExperienceHighlightEntity>,
) {
    fun isEmpty(): Boolean = roles.isEmpty()
}

@Entity(tableName = "rcv_experience_role")
data class RcvExperienceRoleEntity(
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
data class RcvExperienceHighlightEntity(
    @PrimaryKey val id: String,
    val roleId: String,
    val text: String,
    val sortIndex: Int,
)
