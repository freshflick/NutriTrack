package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories

import ClinicDatabase
import android.content.Context
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Insight.Insight
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Insight.InsightDao
import kotlinx.coroutines.flow.Flow

class InsightsRepo(context: Context) {

    private val insightDao: InsightDao

    init {
        val db = ClinicDatabase.getDatabase(context)
        insightDao = db.insightDao()
    }
    // ------------------------ Insight Methods ------------------------

    suspend fun insertInsight(insight: Insight) {
        insightDao.insert(insight)
    }

    suspend fun updateInsight(insight: Insight) {
        insightDao.update(insight)
    }

    suspend fun deleteInsight(insight: Insight) {
        insightDao.delete(insight)
    }

    fun getInsightsForPatient(patientId: Int): Flow<List<Insight>> {
        return insightDao.getInsightsForPatient(patientId)
    }

    suspend fun getInsightForPatientItems(patientID: Int): Insight? {
        return insightDao.getInsightForPatientItems(patientID)
    }

    fun getAllInsights(): Flow<List<Insight>> = insightDao.getAllInsights()
}