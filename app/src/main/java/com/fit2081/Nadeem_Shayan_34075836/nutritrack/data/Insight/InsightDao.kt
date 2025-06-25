package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Insight

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines the data access object (DAO) for the insight entity
 */
@Dao
interface InsightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(insight: Insight)

    @Update
    suspend fun update(insight: Insight)

    @Delete
    suspend fun delete(insight: Insight)

    @Query("SELECT * FROM Insight")
    fun getAllInsights(): Flow<List<Insight>>

    @Query("SELECT * FROM Insight WHERE patientOwnerId = :patientId")
    fun getInsightsForPatient(patientId: Int): Flow<List<Insight>>

    @Query("SELECT * FROM Insight WHERE patientOwnerId = :patientID LIMIT 1")
    suspend fun getInsightForPatientItems(patientID: Int): Insight?

    @Query("DELETE FROM Insight")
    suspend fun deleteAllInsights()
}