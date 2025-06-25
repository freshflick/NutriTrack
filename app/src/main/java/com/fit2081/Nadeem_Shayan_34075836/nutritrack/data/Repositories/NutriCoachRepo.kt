package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories

import ClinicDatabase
import android.content.Context
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FruityVice.Fruit
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FruityVice.RetrofitClient
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.NutriCoachTip
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.NutriCoachTipDao
import kotlinx.coroutines.flow.Flow

class NutriCoachRepo(context: Context) {

    private val nutriCoachTipDao: NutriCoachTipDao

    init {
        val db = ClinicDatabase.getDatabase(context)
        nutriCoachTipDao = db.nutriCoachTipDao()
    }

    // ------------------------ API Call Methods ------------------------
    private val api = RetrofitClient.api

    suspend fun getFruitByName(name: String): Fruit? {
        return try {
            api.getFruit(name)
        } catch (e: Exception) {
            null
        }
    }
    // ------------------------ NutriCoachTip Methods ------------------------

    suspend fun insertTip(tip: NutriCoachTip) {
        nutriCoachTipDao.insertTip(tip)
    }

    suspend fun deleteTip(tip: NutriCoachTip) {
        nutriCoachTipDao.deleteTip(tip)
    }

    suspend fun getTipById(tipId: Int): NutriCoachTip? {
        return nutriCoachTipDao.getTipById(tipId)
    }

    suspend fun clearAllTipsByPatientId(patientId: Int) {
        nutriCoachTipDao.deleteTipsByPatientId(patientId)
    }

    fun getTipsForPatient(patientId: Int): Flow<List<NutriCoachTip>> {
        return nutriCoachTipDao.getTipsByPatientId(patientId)
    }
}