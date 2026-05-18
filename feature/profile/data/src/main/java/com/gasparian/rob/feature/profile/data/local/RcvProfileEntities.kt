package com.gasparian.rob.feature.profile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class RcvProfileEntityGraph(
    val profile: RcvProfileEntity,
    val location: RcvProfileLocationEntity,
    val contact: RcvProfileContactEntity,
    val qualifications: List<RcvProfileQualificationEntity>,
)

@Entity(tableName = "rcv_profile")
data class RcvProfileEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val headline: String,
    val shortBio: String,
    val locationId: String,
    val contactId: String,
    val professionalProfile: String,
    val updatedAtMillis: Long,
)

@Entity(tableName = "rcv_profile_location")
data class RcvProfileLocationEntity(
    @PrimaryKey val id: String,
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

@Entity(tableName = "rcv_profile_contact")
data class RcvProfileContactEntity(
    @PrimaryKey val id: String,
    val email: String,
    val phone: String,
    val linkedin: String,
)

@Entity(tableName = "rcv_profile_qualification")
data class RcvProfileQualificationEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val title: String,
    val description: String,
    val sortIndex: Int,
)
