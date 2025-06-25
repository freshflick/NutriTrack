package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FoodIntake

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


/**
 * This interface defines the data access object (DAO) for the foodintake entity
 */
@Dao
interface FoodIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodIntake: FoodIntake)

    @Update
    suspend fun update(foodIntake: FoodIntake)

    @Delete
    suspend fun delete(foodIntake: FoodIntake)

    @Query("SELECT * FROM FoodIntake WHERE patientOwnerId = :patientID ORDER BY foodIntakeId ASC")
    fun getFoodIntakeByPatient(patientID: Int): Flow<List<FoodIntake>>

    @Query("SELECT * FROM FoodIntake WHERE patientOwnerId = :patientID LIMIT 1")
    suspend fun getFoodIntakeByPatientItems(patientID: Int): FoodIntake?

    @Query("DELETE FROM FoodIntake WHERE patientOwnerId = :patientID")
    suspend fun deleteByPatient(patientID: Int)

    @Query("SELECT * FROM FoodIntake ORDER BY foodIntakeId ASC")
    fun getAllFoodIntake(): Flow<List<FoodIntake>>

    //set big meal time
    @Query("UPDATE FoodIntake SET bigMealTime = :bigMealTime WHERE patientOwnerId = :userId")
    suspend fun updateBigMealTime(userId: Int, bigMealTime: String)

    //set sleep time
    @Query("UPDATE FoodIntake SET sleepTime = :sleepTime WHERE patientOwnerId = :userId")
    suspend fun updateSleepTime(userId: Int, sleepTime: String)

    //set wake up time
    @Query("UPDATE FoodIntake SET wakeUpTime = :wakeUpTime WHERE patientOwnerId = :userId")
    suspend fun updateWakeUpTime(userId: Int, wakeUpTime: String)

    //set persona
    @Query("UPDATE FoodIntake SET persona = :persona WHERE patientOwnerId = :userId")
    suspend fun updatePersona(userId: Int, persona: String)

    fun foodIntakeToMap(intake: FoodIntake): Map<String, Boolean> {
        return mapOf(
            "Fruits" to intake.fruits,
            "Vegetables" to intake.vegetables,
            "Grains" to intake.grains,
            "Red meat" to intake.redMeat,
            "Seafood" to intake.seaFood,
            "Poultry" to intake.poultry,
            "Fish" to intake.fish,
            "Eggs" to intake.eggs,
            "Nuts/Seeds" to intake.nutsSeeds
        )
    }
}
