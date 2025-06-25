package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.Patient
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivityViewModel(application: Application, private val repository: PatientRepo)
    : AndroidViewModel(application) {
    fun importCSVIfFirstLaunch() {
        // Run CSV import in background
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val csvData = readAllCsv(context = context, "participants.csv")

            val patientsToInsert = mutableListOf<Patient>()
            csvData.forEach { patient ->
                val existingPatient = repository.getPatientByUserId(patient.userId)

                if (existingPatient == null) {
                    patientsToInsert.add(patient)  // Add patient to list if not exists
                    Log.d("PatientCheck", "Patient added to insert list: $patient")
                } else {
                    Log.d("Patient exists", "Patient exists: $existingPatient")
                    repository.updatePatient(patient)
                }
            }

            // Insert all patients in a transaction if there are any new patients
            if (patientsToInsert.isNotEmpty()) {
                repository.insertPatients(patientsToInsert)
                Log.d("PatientCheck", "Patients inserted: ${patientsToInsert.size}")
            }

            // Log all patients by collecting from the Flow
            repository.getAllPatients().collect { patientList ->
                patientList.forEach { insertedPatient ->
                    Log.d("PatientCheck", "Inserted patient: $insertedPatient")
                }
            }
        }
    }
}

//logic for handling all csv data to later be added to database
fun readAllCsv(context: Context, fileName: String): List<Patient> {
    val patientList = mutableListOf<Patient>()
    try {
        context.assets.open(fileName).bufferedReader().use { reader ->
            val headerLine = reader.readLine()
            val headers = headerLine.split(",")

            // Create a map of column names to their index
            val columnIndex = headers.withIndex().associate { it.value to it.index }

            reader.forEachLine { line ->
                val tokens = line.split(",")

                // Use safe extraction with default values where needed
                fun getString(key: String) = tokens.getOrNull(columnIndex[key] ?: -1)?.trim() ?: ""
                fun getInt(key: String) = getString(key).toIntOrNull() ?: 0
                fun getLong(key: String) = getString(key).toLongOrNull() ?: 0L
                fun getFloat(key: String): Float {
                    return String.format(Locale.US, "%.2f", getString(key).toFloatOrNull() ?: 0f).toFloat()
                }

                val sex = getString("Sex")

                fun genderedFloat(key: String): Float {
                    return if (
                        (key.contains("Male") && sex.equals("Male", ignoreCase = true)) ||
                        (key.contains("Female") && sex.equals("Female", ignoreCase = true))
                    ) {
                        getFloat(key)
                    } else {
                        0f
                    }
                }

                val patient = Patient(
                    name = "Unknown",
                    phoneNumber = getLong("PhoneNumber"),
                    userId = getInt("User_ID"),
                    sex = sex,
                    password = "Unknown",

                    //HEIFA scores
                    heifaTotalScoreMale = genderedFloat("HEIFAtotalscoreMale"),
                    heifaTotalScoreFemale = genderedFloat("HEIFAtotalscoreFemale"),
                    discretionaryHeifaScoreMale = genderedFloat("DiscretionaryHEIFAscoreMale"),
                    discretionaryHeifaScoreFemale = genderedFloat("DiscretionaryHEIFAscoreFemale"),

                    //Serving sizes
                    discretionaryServeSize = getFloat("Discretionaryservesize"),

                    //Vegetables
                    vegetablesHeifaScoreMale = genderedFloat("VegetablesHEIFAscoreMale"),
                    vegetablesHeifaScoreFemale = genderedFloat("VegetablesHEIFAscoreFemale"),
                    vegetablesWithLegumesAllocatedServeSize = getFloat("Vegetableswithlegumesallocatedservesize"),
                    legumesAllocatedVegetables = getFloat("LegumesallocatedVegetables"),
                    vegetablesVariationsScore = getFloat("Vegetablesvariationsscore"),
                    vegetablesCruciferous = getFloat("VegetablesCruciferous"),
                    vegetablesTuberAndBulb = getFloat("VegetablesTuberandbulb"),
                    vegetablesOther = getFloat("VegetablesOther"),
                    legumes = getFloat("Legumes"),
                    vegetablesGreen = getFloat("VegetablesGreen"),
                    vegetablesRedAndOrange = getFloat("VegetablesRedandorange"),

                    //Fruits
                    fruitHeifaScoreMale = genderedFloat("FruitHEIFAscoreMale"),
                    fruitHeifaScoreFemale = genderedFloat("FruitHEIFAscoreFemale"),
                    fruitServeSize = getFloat("Fruitservesize"),
                    fruitVariationsScore = getFloat("Fruitvariationsscore"),
                    fruitPome = getFloat("FruitPome"),
                    fruitTropicalAndSubtropical = getFloat("FruitTropicalandsubtropical"),
                    fruitBerry = getFloat("FruitBerry"),
                    fruitStone = getFloat("FruitStone"),
                    fruitCitrus = getFloat("FruitCitrus"),
                    fruitOther = getFloat("FruitOther"),

                    //Grains and cereals
                    grainsAndCerealsHeifaScoreMale = genderedFloat("GrainsandcerealsHEIFAscoreMale"),
                    grainsAndCerealsHeifaScoreFemale = genderedFloat("GrainsandcerealsHEIFAscoreFemale"),
                    grainsAndCerealsServeSize = getFloat("Grainsandcerealsservesize"),
                    grainsAndCerealsNonWholeGrains = getFloat("GrainsandcerealsNonwholegrains"),

                    //Whole grains
                    wholeGrainsHeifaScoreMale = genderedFloat("WholegrainsHEIFAscoreMale"),
                    wholeGrainsHeifaScoreFemale = genderedFloat("WholegrainsHEIFAscoreFemale"),
                    wholeGrainsServeSize = getFloat("Wholegrainsservesize"),

                    //Meat and alternatives
                    meatAndAlternativesHeifaScoreMale = genderedFloat("MeatandalternativesHEIFAscoreMale"),
                    meatAndAlternativesHeifaScoreFemale = genderedFloat("MeatandalternativesHEIFAscoreFemale"),
                    meatAndAlternativesWithLegumesAllocatedServeSize = getFloat("Meatandalternativeswithlegumesallocatedservesize"),
                    legumesAllocatedMeatAndAlternatives = getFloat("LegumesallocatedMeatandalternatives"),

                    //Dairy and alternatives
                    dairyAndAlternativesHeifaScoreMale = genderedFloat("DairyandalternativesHEIFAscoreMale"),
                    dairyAndAlternativesHeifaScoreFemale = genderedFloat("DairyandalternativesHEIFAscoreFemale"),
                    dairyAndAlternativesServeSize = getFloat("Dairyandalternativesservesize"),

                    //Sodium
                    sodiumHeifaScoreMale = genderedFloat("SodiumHEIFAscoreMale"),
                    sodiumHeifaScoreFemale = genderedFloat("SodiumHEIFAscoreFemale"),
                    sodiumMgMilligrams = getFloat("Sodiummgmilligrams"),

                    //Alcohol
                    alcoholHeifaScoreMale = genderedFloat("AlcoholHEIFAscoreMale"),
                    alcoholHeifaScoreFemale = genderedFloat("AlcoholHEIFAscoreFemale"),
                    alcoholStandardDrinks = getFloat("Alcoholstandarddrinks"),

                    //Water
                    waterHeifaScoreMale = genderedFloat("WaterHEIFAscoreMale"),
                    waterHeifaScoreFemale = genderedFloat("WaterHEIFAscoreFemale"),
                    water = getFloat("Water"),
                    waterTotalML = getFloat("WaterTotalmL"),
                    beverageTotalML = getFloat("BeverageTotalmL"),

                    //Sugar
                    sugarHeifaScoreMale = genderedFloat("SugarHEIFAscoreMale"),
                    sugarHeifaScoreFemale = genderedFloat("SugarHEIFAscoreFemale"),
                    sugar = getFloat("Sugar"),

                    //Saturated Fat
                    saturatedFatHeifaScoreMale = genderedFloat("SaturatedFatHEIFAscoreMale"),
                    saturatedFatHeifaScoreFemale = genderedFloat("SaturatedFatHEIFAscoreFemale"),
                    saturatedFat = getFloat("SaturatedFat"),

                    //Unsaturated Fat
                    unsaturatedFatHeifaScoreMale = genderedFloat("UnsaturatedFatHEIFAscoreMale"),
                    unsaturatedFatHeifaScoreFemale = genderedFloat("UnsaturatedFatHEIFAscoreFemale"),
                    unsaturatedFatServeSize = getFloat("UnsaturatedFatservesize")
                )
                patientList.add(patient)
            }
        }
    } catch (e: Exception) {
        Log.e("CSV", "Error reading CSV file", e)
    }
    return patientList
}

