package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FoodIntake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.Patient

@Entity(
    tableName = "FoodIntake",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["userId"],
        childColumns = ["patientOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["patientOwnerId"], unique = true)]

)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true)
    val foodIntakeId: Int = 0,

    val patientOwnerId: Int,  //foreign key

    //boolean questions
    val fruits: Boolean,
    val vegetables: Boolean,
    val grains: Boolean,
    val redMeat: Boolean,
    val seaFood: Boolean,
    val poultry: Boolean,
    val fish: Boolean,
    val eggs: Boolean,
    val nutsSeeds: Boolean,

    //persona
    val persona: String,

    //timings
    val bigMealTime: String?,
    val sleepTime: String?,
    val wakeUpTime: String?
)