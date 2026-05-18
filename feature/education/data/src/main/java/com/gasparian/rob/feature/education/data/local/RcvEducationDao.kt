package com.gasparian.rob.feature.education.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface RcvEducationDao {
    fun educationGraphFlow(): Flow<RcvEducationEntityGraph> = combine(
        institutionsFlow(),
        locationsFlow(),
        itemsFlow(),
    ) { institutions, locations, items ->
        RcvEducationEntityGraph(
            institutions = institutions,
            locations = locations,
            items = items,
        )
    }

    @Transaction
    suspend fun getEducationGraph(): RcvEducationEntityGraph = RcvEducationEntityGraph(
        institutions = getInstitutions(),
        locations = getLocations(),
        items = getItems(),
    )

    @Transaction
    suspend fun replaceEducation(
        graph: RcvEducationEntityGraph,
    ) {
        clearItems()
        clearInstitutions()
        clearLocations()
        upsertLocations(graph.locations)
        upsertInstitutions(graph.institutions)
        upsertItems(graph.items)
    }

    @Query("SELECT * FROM rcv_institution ORDER BY name")
    suspend fun getInstitutions(): List<RcvInstitutionEntity>

    @Query("SELECT * FROM rcv_institution ORDER BY name")
    fun institutionsFlow(): Flow<List<RcvInstitutionEntity>>

    @Query("SELECT * FROM rcv_education_location")
    suspend fun getLocations(): List<RcvEducationLocationEntity>

    @Query("SELECT * FROM rcv_education_location")
    fun locationsFlow(): Flow<List<RcvEducationLocationEntity>>

    @Query("SELECT * FROM rcv_education_item ORDER BY startDate DESC")
    suspend fun getItems(): List<RcvEducationItemEntity>

    @Query("SELECT * FROM rcv_education_item ORDER BY startDate DESC")
    fun itemsFlow(): Flow<List<RcvEducationItemEntity>>

    @Upsert
    suspend fun upsertInstitutions(
        institutions: List<RcvInstitutionEntity>,
    )

    @Upsert
    suspend fun upsertLocations(
        locations: List<RcvEducationLocationEntity>,
    )

    @Upsert
    suspend fun upsertItems(
        items: List<RcvEducationItemEntity>,
    )

    @Query("DELETE FROM rcv_institution")
    suspend fun clearInstitutions()

    @Query("DELETE FROM rcv_education_location")
    suspend fun clearLocations()

    @Query("DELETE FROM rcv_education_item")
    suspend fun clearItems()
}
