package com.gasparian.rob.feature.education.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class RcvEducationEntityGraph(
    val institutions: List<RcvInstitutionEntity>,
    val locations: List<RcvEducationLocationEntity>,
    val items: List<RcvEducationItemEntity>,
) {
    fun isEmpty(): Boolean = institutions.isEmpty() && items.isEmpty()
}

@Entity(tableName = "rcv_institution")
data class RcvInstitutionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortName: String?,
    val type: String,
    val description: String,
    val websiteUrl: String,
    val locationId: String,
)

@Entity(tableName = "rcv_education_location")
data class RcvEducationLocationEntity(
    @PrimaryKey val id: String,
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

@Entity(tableName = "rcv_education_item")
data class RcvEducationItemEntity(
    @PrimaryKey val id: String,
    val institutionId: String,
    val facultyName: String?,
    val programName: String,
    val credential: String,
    val fieldOfStudy: String,
    val startDate: String,
    val endDate: String,
    val status: String,
)
