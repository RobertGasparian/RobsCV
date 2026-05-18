package com.gasparian.rob.feature.profile.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface RcvProfileDao {
    fun profileGraphFlow(): Flow<RcvProfileEntityGraph?> = combine(
        profileFlow(),
        locationsFlow(),
        contactsFlow(),
        qualificationsFlow(),
    ) { profile, locations, contacts, qualifications ->
        profile ?: return@combine null
        RcvProfileEntityGraph(
            profile = profile,
            location = locations.firstOrNull { location -> location.id == profile.locationId } ?: return@combine null,
            contact = contacts.firstOrNull { contact -> contact.id == profile.contactId } ?: return@combine null,
            qualifications = qualifications.filter { qualification -> qualification.profileId == profile.id },
        )
    }

    @Transaction
    suspend fun getProfileGraph(): RcvProfileEntityGraph? {
        val profile = getProfile() ?: return null
        return RcvProfileEntityGraph(
            profile = profile,
            location = getLocation(profile.locationId) ?: return null,
            contact = getContact(profile.contactId) ?: return null,
            qualifications = getQualifications(profile.id),
        )
    }

    @Transaction
    suspend fun replaceProfile(
        graph: RcvProfileEntityGraph,
    ) {
        clearQualifications()
        clearProfile()
        clearContacts()
        clearLocations()
        upsertLocation(graph.location)
        upsertContact(graph.contact)
        upsertProfile(graph.profile)
        upsertQualifications(graph.qualifications)
    }

    @Query("SELECT * FROM rcv_profile LIMIT 1")
    suspend fun getProfile(): RcvProfileEntity?

    @Query("SELECT * FROM rcv_profile LIMIT 1")
    fun profileFlow(): Flow<RcvProfileEntity?>

    @Query("SELECT * FROM rcv_profile_location")
    fun locationsFlow(): Flow<List<RcvProfileLocationEntity>>

    @Query("SELECT * FROM rcv_profile_contact")
    fun contactsFlow(): Flow<List<RcvProfileContactEntity>>

    @Query("SELECT * FROM rcv_profile_qualification ORDER BY sortIndex")
    fun qualificationsFlow(): Flow<List<RcvProfileQualificationEntity>>

    @Query("SELECT * FROM rcv_profile_location WHERE id = :id")
    suspend fun getLocation(
        id: String,
    ): RcvProfileLocationEntity?

    @Query("SELECT * FROM rcv_profile_contact WHERE id = :id")
    suspend fun getContact(
        id: String,
    ): RcvProfileContactEntity?

    @Query("SELECT * FROM rcv_profile_qualification WHERE profileId = :profileId ORDER BY sortIndex")
    suspend fun getQualifications(
        profileId: String,
    ): List<RcvProfileQualificationEntity>

    @Upsert
    suspend fun upsertProfile(
        profile: RcvProfileEntity,
    )

    @Upsert
    suspend fun upsertLocation(
        location: RcvProfileLocationEntity,
    )

    @Upsert
    suspend fun upsertContact(
        contact: RcvProfileContactEntity,
    )

    @Upsert
    suspend fun upsertQualifications(
        qualifications: List<RcvProfileQualificationEntity>,
    )

    @Query("DELETE FROM rcv_profile")
    suspend fun clearProfile()

    @Query("DELETE FROM rcv_profile_location")
    suspend fun clearLocations()

    @Query("DELETE FROM rcv_profile_contact")
    suspend fun clearContacts()

    @Query("DELETE FROM rcv_profile_qualification")
    suspend fun clearQualifications()
}
