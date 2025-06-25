package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Insight.Insight
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.InsightsRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsightViewModel(private val repository: InsightsRepo, private val patientRepo: PatientRepo) : ViewModel() {
    val vegetables = MutableLiveData<Double?>()
    val fruits = MutableLiveData<Double?>()
    val grainsAndCereals = MutableLiveData<Double?>()
    val wholeGrains = MutableLiveData<Double?>()
    val meatAndAlternatives = MutableLiveData<Double?>()
    val dairy = MutableLiveData<Double?>()
    val water = MutableLiveData<Double?>()
    val unsaturatedFats = MutableLiveData<Double?>()
    val saturatedFats = MutableLiveData<Double?>()
    val sodium = MutableLiveData<Double?>()
    val sugar = MutableLiveData<Double?>()
    val alcohol = MutableLiveData<Double?>()
    val discretionaryFood = MutableLiveData<Double?>()
    val heifaScore = MutableLiveData<Double?>()

    val insightScoreMap = mapOf(
        "Vegetables" to vegetables,
        "Fruits" to fruits,
        "Grains & Cereals" to grainsAndCereals,
        "Whole Grains" to wholeGrains,
        "Meat & Alternatives" to meatAndAlternatives,
        "Dairy" to dairy,
        "Water" to water,
        "Unsaturated Fats" to unsaturatedFats,
        "Saturated Fats" to saturatedFats,
        "Sodium" to sodium,
        "Sugar" to sugar,
        "Alcohol" to alcohol,
        "Discretionary Foods" to discretionaryFood,
        "Total Food Quality Score" to heifaScore
    )

    private val _insightStatus = MutableLiveData<Boolean>()
    val insightStatus: LiveData<Boolean> = _insightStatus

    //load and display insight scores
    fun loadInsightScores(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId == null) return@launch
            _insightStatus

            val savedSelections = repository.getInsightForPatientItems(userId)
            if (savedSelections != null) {
                vegetables.postValue(savedSelections.vegetableScore)
                fruits.postValue(savedSelections.fruitScore)
                grainsAndCereals.postValue(savedSelections.grainsAndCerealScore)
                wholeGrains.postValue(savedSelections.wholeGrainScore)
                meatAndAlternatives.postValue(savedSelections.meatAndAlternativesScore)
                dairy.postValue(savedSelections.dairyScore)
                water.postValue(savedSelections.waterScore)
                unsaturatedFats.postValue(savedSelections.unsaturatedFatScore)
                saturatedFats.postValue(savedSelections.saturatedFatScore)
                sodium.postValue(savedSelections.sodiumScore)
                sugar.postValue(savedSelections.sugarScore)
                alcohol.postValue(savedSelections.alcoholScore)
                discretionaryFood.postValue(savedSelections.discretionaryFoodScore)
                heifaScore.postValue(savedSelections.totalFoodScore)
            }
            else {
                Log.d("InsightLoad", "No insight data found for user $userId – skipping load.")
            }
        }
    }

    // Insight category logic --------------------------------------------
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun addInsight(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId == null) return@launch
            _isLoading.postValue(true)
            Log.d("InsightDebug", "Loading set to true")

            val scoreData = patientRepo.getHeifaScores(userId)
            val gender = patientRepo.getPatientSex(userId)

            if (scoreData != null) {
                val isMale = gender.trim().equals("male", ignoreCase = true)

                val insight = Insight(
                    patientOwnerId = userId,
                    vegetableScore = (if (isMale) scoreData.vegetablesHeifaScoreMale else scoreData.vegetablesHeifaScoreFemale),
                    fruitScore = if (isMale) scoreData.fruitHeifaScoreMale else scoreData.fruitHeifaScoreFemale,
                    grainsAndCerealScore = if (isMale) scoreData.grainsAndCerealsHeifaScoreMale else scoreData.grainsAndCerealsHeifaScoreFemale,
                    wholeGrainScore = if (isMale) scoreData.wholeGrainsHeifaScoreMale else scoreData.wholeGrainsHeifaScoreFemale,
                    meatAndAlternativesScore = if (isMale) scoreData.meatAndAlternativesHeifaScoreMale else scoreData.meatAndAlternativesHeifaScoreFemale,
                    dairyScore = if (isMale) scoreData.dairyAndAlternativesHeifaScoreMale else scoreData.dairyAndAlternativesHeifaScoreFemale,
                    waterScore = if (isMale) scoreData.waterHeifaScoreMale else scoreData.waterHeifaScoreFemale,
                    unsaturatedFatScore = if (isMale) scoreData.unsaturatedFatHeifaScoreMale else scoreData.unsaturatedFatHeifaScoreFemale,
                    saturatedFatScore = if (isMale) scoreData.saturatedFatHeifaScoreMale else scoreData.saturatedFatHeifaScoreFemale,
                    sodiumScore = if (isMale) scoreData.sodiumHeifaScoreMale else scoreData.sodiumHeifaScoreFemale,
                    sugarScore = if (isMale) scoreData.sugarHeifaScoreMale else scoreData.sugarHeifaScoreFemale,
                    alcoholScore = if (isMale) scoreData.alcoholHeifaScoreMale else scoreData.alcoholHeifaScoreFemale,
                    discretionaryFoodScore = if (isMale) scoreData.discretionaryHeifaScoreMale else scoreData.discretionaryHeifaScoreFemale,
                    totalFoodScore = if(isMale) scoreData.heifaTotalScoreMale else scoreData.heifaTotalScoreFemale
                )
                repository.insertInsight(insight)
                Log.d("InsightDebug", "Insight inserted")
            } else {
                Log.d("Insight", "Missing score or gender data for userId=$userId")
            }
            _isLoading.postValue(false)
            Log.d("InsightDebug", "Loading set to false")
        }
    }
}