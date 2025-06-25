package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories

import ClinicDatabase
import android.content.Context
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.AuthManager
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.BasicInfo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.FruitInfo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.HeifaScores
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.HomeScreenInfo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.Patient
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.PatientDao
import kotlinx.coroutines.flow.Flow

class PatientRepo(context: Context) {

    private val patientDao: PatientDao

    init {
        val db = ClinicDatabase.getDatabase(context)
        patientDao = db.patientDao()
    }
    // ------------------------ Patient Methods ------------------------

    suspend fun login(userId: Int, password: String): Boolean {
        return AuthManager.login(patientDao, userId, password)
    }

    suspend fun insertPatient(patient: Patient) {
        patientDao.insert(patient)
    }

    suspend fun deletePatient(patient: Patient) {
        patientDao.delete(patient)
    }

    suspend fun updatePatient(patient: Patient) {
        patientDao.update(patient)
    }

    suspend fun deleteAllPatients() {
        patientDao.deleteAllPatients()
    }

    suspend fun getBasicInfo(userId: Int): BasicInfo {
        return patientDao.getBasicInfo(userId)
    }

    suspend fun getHomeScreenInfo(userId: Int): HomeScreenInfo? {
        return patientDao.getHomeScreenInfo(userId)
    }

    suspend fun getPatientSex(userId: Int): String {
        return patientDao.getPatientSex(userId)
    }

    suspend fun getHeifaScores(userId: Int): HeifaScores? {
        return patientDao.getHeifaScores(userId)
    }

    suspend fun getPatientByUserId(userId: Int): Patient? {
        return patientDao.getPatientByUserId(userId)
    }

    suspend fun insertPatients(patients: List<Patient>) {
        patientDao.insertPatientsInTransaction(patients)
    }

    suspend fun updateBasicInfo(userId: Int, name: String, password: String) {
        return patientDao.updateBasicInfo(userId, name, password)
    }

    suspend fun getFruitScoreInfo(userId: Int): FruitInfo {
        return patientDao.getFruitInfo(userId)
    }

    fun getAllPatientIds(): Flow<List<Int>> = patientDao.getAllPatientIds()

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()
}