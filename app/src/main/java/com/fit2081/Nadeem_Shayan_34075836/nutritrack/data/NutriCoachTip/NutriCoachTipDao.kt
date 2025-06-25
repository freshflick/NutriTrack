package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines the data access object (DAO) for the nutricoachtip entity
 */

@Dao
interface NutriCoachTipDao {

    // Insert a single tip
    @Insert
    suspend fun insertTip(tip: NutriCoachTip)

    // Insert multiple tips at once
    @Insert
    suspend fun insertAllTips(tips: List<NutriCoachTip>)

    // Get all tips
    @Query("SELECT * FROM NutriCoachTip")
    fun getAllTips(): Flow<List<NutriCoachTip>>?

    // Get a tip by its ID
    @Query("SELECT * FROM NutriCoachTip WHERE tipId = :id")
    suspend fun getTipById(id: Int): NutriCoachTip?

    // Delete a tip
    @Delete
    suspend fun deleteTip(tip: NutriCoachTip)

    // Update a tip
    @Update
    suspend fun updateTip(tip: NutriCoachTip)

    // Delete all tips
    @Query("DELETE FROM NutriCoachTip WHERE patientOwnerId = :patientId")
    suspend fun deleteTipsByPatientId(patientId: Int)

    @Query("SELECT * FROM NutriCoachTip WHERE patientOwnerId = :patientId")
    fun getTipsByPatientId(patientId: Int): Flow<List<NutriCoachTip>>
}