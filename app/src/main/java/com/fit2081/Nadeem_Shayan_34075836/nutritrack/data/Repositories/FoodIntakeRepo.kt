package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories

import ClinicDatabase
import android.content.Context
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FoodIntake.FoodIntake
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FoodIntake.FoodIntakeDao
import kotlinx.coroutines.flow.Flow

class FoodIntakeRepo(context: Context) {

    private val foodIntakeDao: FoodIntakeDao

    init {
        val db = ClinicDatabase.getDatabase(context)
        foodIntakeDao = db.foodIntakeDao()
    }

    // ------------------------ FoodIntake Methods ------------------------

    suspend fun insertFoodIntake(foodIntake: FoodIntake) {
        foodIntakeDao.insert(foodIntake)
    }

    suspend fun updateFoodIntake(foodIntake: FoodIntake) {
        foodIntakeDao.update(foodIntake)
    }

    suspend fun deleteFoodIntake(foodIntake: FoodIntake) {
        foodIntakeDao.delete(foodIntake)
    }

    suspend fun getFoodIntakeByPatientItems(patientID: Int): FoodIntake? {
        return foodIntakeDao.getFoodIntakeByPatientItems(patientID)
    }

    fun getAllFoodIntake(): Flow<List<FoodIntake>> = foodIntakeDao.getAllFoodIntake()
}