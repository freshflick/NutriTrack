package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.Patient

@Entity(
    tableName = "NutriCoachTip",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["userId"],
        childColumns = ["patientOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["patientOwnerId"])]
)
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true)
    val tipId: Int = 0,

    val patientOwnerId: Int,
    val tipText: String
)