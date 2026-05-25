package com.gasparian.rob.feature.education.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface EducationDao {
    @Transaction
    @Query("SELECT * FROM rcv_institution ORDER BY name")
    fun institutionsWithEducationFlow(): Flow<List<InstitutionWithEducationEntity>>

    fun educationGraphFlow(): Flow<EducationEntityReadGraph> = institutionsWithEducationFlow()
        .map { institutions ->
            EducationEntityReadGraph(
                institutions = institutions,
            )
        }

    @Transaction
    suspend fun getEducationGraph(): EducationEntityGraph = EducationEntityGraph(
        institutions = getInstitutions(),
        locations = getLocations(),
        items = getItems(),
    )

    @Transaction
    suspend fun replaceEducation(
        graph: EducationEntityGraph,
    ) {
        clearEducationCache()
        upsertLocations(graph.locations)
        upsertInstitutions(graph.institutions)
        upsertItems(graph.items)
    }

    @Transaction
    suspend fun clearEducationCache() {
        clearItems()
        clearInstitutions()
        clearLocations()
    }

    @Query("SELECT * FROM rcv_institution ORDER BY name")
    suspend fun getInstitutions(): List<InstitutionEntity>

    @Transaction
    @Query("SELECT * FROM rcv_institution ORDER BY name")
    suspend fun getInstitutionsWithEducation(): List<InstitutionWithEducationEntity>

    @Query("SELECT * FROM rcv_institution ORDER BY name")
    fun institutionsFlow(): Flow<List<InstitutionEntity>>

    @Query("SELECT * FROM rcv_education_location")
    suspend fun getLocations(): List<EducationLocationEntity>

    @Query("SELECT * FROM rcv_education_location")
    fun locationsFlow(): Flow<List<EducationLocationEntity>>

    @Query("SELECT * FROM rcv_education_item ORDER BY startDate DESC")
    suspend fun getItems(): List<EducationItemEntity>

    @Query("SELECT * FROM rcv_education_item ORDER BY startDate DESC")
    fun itemsFlow(): Flow<List<EducationItemEntity>>

    @Upsert
    suspend fun upsertInstitutions(
        institutions: List<InstitutionEntity>,
    )

    @Upsert
    suspend fun upsertLocations(
        locations: List<EducationLocationEntity>,
    )

    @Upsert
    suspend fun upsertItems(
        items: List<EducationItemEntity>,
    )

    @Query("DELETE FROM rcv_institution")
    suspend fun clearInstitutions()

    @Query("DELETE FROM rcv_education_location")
    suspend fun clearLocations()

    @Query("DELETE FROM rcv_education_item")
    suspend fun clearItems()
}
