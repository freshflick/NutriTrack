package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Insight

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.Patient

@Entity(
    tableName = "Insight",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["userId"],
        childColumns = ["patientOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["patientOwnerId"], unique = true)]
)

data class Insight(
    @PrimaryKey(autoGenerate = true)
    val insightId: Int = 0,

    val patientOwnerId: Int,  // Foreign key column that links to Patient.userId

    var vegetableScore: Double?,
    var fruitScore: Double?,
    var grainsAndCerealScore: Double?,
    var wholeGrainScore: Double?,
    var meatAndAlternativesScore: Double?,
    var dairyScore: Double?,
    var waterScore: Double?,
    var unsaturatedFatScore: Double?,
    var saturatedFatScore: Double?,
    var sodiumScore: Double?,
    var sugarScore: Double?,
    var alcoholScore: Double?,
    var discretionaryFoodScore: Double?,
    val totalFoodScore: Double?
)