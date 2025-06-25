package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
/**
 * This interface defines the data access object (DAO) for the patient entity
 */
@Dao
interface PatientDao {

    // Inserts a new patient into the database.
    @Insert
    suspend fun insert(patient: Patient)

    // Updates an existing patient in the database.
    @Update
    suspend fun update(patient: Patient)

    // Deletes a specific patient from the database.
    @Delete
    suspend fun delete(patient: Patient)

    @Query("SELECT * FROM Patient WHERE userId = :userId LIMIT 1")
    suspend fun getPatientByUserId(userId: Int): Patient?

    //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<Patient>)

    @Transaction
    suspend fun insertPatientsInTransaction(patients: List<Patient>) {
        insertPatients(patients)  // This inserts all patients in a single transaction
    }
    //

    @Query("SELECT name, sex, heifaTotalScoreMale, heifaTotalScoreFemale FROM Patient WHERE userId = :userId LIMIT 1")
    suspend fun getHomeScreenInfo(userId: Int): HomeScreenInfo?

    @Query("SELECT name, phoneNumber, userId FROM Patient WHERE userId = :userId LIMIT 1")
    suspend fun getBasicInfo(userId: Int): BasicInfo

    @Query("SELECT fruitHeifaScoreMale, fruitHeifaScoreFemale, fruitServeSize, fruitVariationsScore FROM Patient WHERE userId = :userId LIMIT 1")
    suspend fun getFruitInfo(userId: Int): FruitInfo

    @Query("SELECT sex FROM Patient WHERE userId = :userId LIMIT 1")
    suspend fun getPatientSex(userId: Int): String

    @Query("SELECT userId FROM Patient")
    fun getAllPatientIds(): Flow<List<Int>>

    @Query("""
    SELECT 
        heifaTotalScoreMale, heifaTotalScoreFemale,
        discretionaryHeifaScoreMale, discretionaryHeifaScoreFemale,
        vegetablesHeifaScoreMale, vegetablesHeifaScoreFemale,
        fruitHeifaScoreMale, fruitHeifaScoreFemale,
        grainsAndCerealsHeifaScoreMale, grainsAndCerealsHeifaScoreFemale,
        wholeGrainsHeifaScoreMale, wholeGrainsHeifaScoreFemale,
        meatAndAlternativesHeifaScoreMale, meatAndAlternativesHeifaScoreFemale,
        dairyAndAlternativesHeifaScoreMale, dairyAndAlternativesHeifaScoreFemale,
        sodiumHeifaScoreMale, sodiumHeifaScoreFemale,
        alcoholHeifaScoreMale, alcoholHeifaScoreFemale,
        waterHeifaScoreMale, waterHeifaScoreFemale,
        sugarHeifaScoreMale, sugarHeifaScoreFemale,
        saturatedFatHeifaScoreMale, saturatedFatHeifaScoreFemale,
        unsaturatedFatHeifaScoreMale, unsaturatedFatHeifaScoreFemale
    FROM Patient WHERE userId = :userId LIMIT 1
    """)
    suspend fun getHeifaScores(userId: Int): HeifaScores?

    // Deletes all patients from the database.
    @Query("DELETE FROM Patient")
    suspend fun deleteAllPatients()

    @Query("UPDATE Patient SET password = :password WHERE userId = :userId")
    suspend fun updatePassword(userId: Int, password: String)

    @Query("UPDATE Patient SET name = :name WHERE userId = :userId")
    suspend fun updateName(userId: Int, name: String)

    @Query("UPDATE Patient SET name = :name, password = :password WHERE userId = :userId")
    suspend fun updateBasicInfo(userId: Int, name: String, password: String)

    // Retrieves all patients from the database, ordered by their ID in ascending order.
    @Query("SELECT * FROM Patient ORDER BY userId ASC")
    fun getAllPatients(): Flow<List<Patient>>
}

data class FruitInfo(
    val fruitHeifaScoreMale: Double?,
    val fruitHeifaScoreFemale: Double?,
    val fruitServeSize: Float?,
    val fruitVariationsScore: Float?
)

data class BasicInfo(
    val name: String,
    val phoneNumber: Long,
    val userId: Int
)

data class HomeScreenInfo(
    val name: String,
    val sex: String,
    val heifaTotalScoreMale: Double?,
    val heifaTotalScoreFemale: Double?
)

data class HeifaScores(
    val heifaTotalScoreMale: Double?,
    val heifaTotalScoreFemale: Double?,
    val discretionaryHeifaScoreMale: Double?,
    val discretionaryHeifaScoreFemale: Double?,
    val vegetablesHeifaScoreMale: Double?,
    val vegetablesHeifaScoreFemale: Double?,
    val fruitHeifaScoreMale: Double?,
    val fruitHeifaScoreFemale: Double?,
    val grainsAndCerealsHeifaScoreMale: Double?,
    val grainsAndCerealsHeifaScoreFemale: Double?,
    val wholeGrainsHeifaScoreMale: Double?,
    val wholeGrainsHeifaScoreFemale: Double?,
    val meatAndAlternativesHeifaScoreMale: Double?,
    val meatAndAlternativesHeifaScoreFemale: Double?,
    val dairyAndAlternativesHeifaScoreMale: Double?,
    val dairyAndAlternativesHeifaScoreFemale: Double?,
    val sodiumHeifaScoreMale: Double?,
    val sodiumHeifaScoreFemale: Double?,
    val alcoholHeifaScoreMale: Double?,
    val alcoholHeifaScoreFemale: Double?,
    val waterHeifaScoreMale: Double?,
    val waterHeifaScoreFemale: Double?,
    val sugarHeifaScoreMale: Double?,
    val sugarHeifaScoreFemale: Double?,
    val saturatedFatHeifaScoreMale: Double?,
    val saturatedFatHeifaScoreFemale: Double?,
    val unsaturatedFatHeifaScoreMale: Double?,
    val unsaturatedFatHeifaScoreFemale: Double?
)
