package com.gasparian.rob.feature.education.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class EducationEntityGraph(
    val institutions: List<InstitutionEntity>,
    val locations: List<EducationLocationEntity>,
    val items: List<EducationItemEntity>,
) {
    fun isEmpty(): Boolean = institutions.isEmpty() && items.isEmpty()
}

data class EducationEntityReadGraph(
    val institutions: List<InstitutionWithEducationEntity>,
) {
    fun isEmpty(): Boolean = institutions.isEmpty()
}

data class InstitutionWithEducationEntity(
    @Embedded val institution: InstitutionEntity,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "id",
    )
    val location: EducationLocationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "institutionId",
    )
    val items: List<EducationItemEntity>,
)

@Entity(tableName = "rcv_institution")
data class InstitutionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortName: String?,
    val type: String,
    val description: String,
    val websiteUrl: String,
    val locationId: String,
)

@Entity(tableName = "rcv_education_location")
data class EducationLocationEntity(
    @PrimaryKey val id: String,
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

@Entity(tableName = "rcv_education_item")
data class EducationItemEntity(
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
