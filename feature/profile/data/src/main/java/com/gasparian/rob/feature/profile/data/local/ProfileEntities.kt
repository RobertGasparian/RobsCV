package com.gasparian.rob.feature.profile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ProfileEntityGraph(
    val profile: ProfileEntity,
    val location: ProfileLocationEntity,
    val contact: ProfileContactEntity,
    val qualifications: List<ProfileQualificationEntity>,
)

@Entity(tableName = "rcv_profile")
data class ProfileEntity(
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
data class ProfileLocationEntity(
    @PrimaryKey val id: String,
    val city: String,
    val region: String?,
    val country: String,
    val addressLine: String?,
)

@Entity(tableName = "rcv_profile_contact")
data class ProfileContactEntity(
    @PrimaryKey val id: String,
    val email: String,
    val phone: String,
    val linkedin: String,
)

@Entity(tableName = "rcv_profile_qualification")
data class ProfileQualificationEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val title: String,
    val description: String,
    val sortIndex: Int,
)
