package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.BuildConfig
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.NutriCoachTip
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.UiState
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.FoodIntakeRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.NutriCoachRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class GenAIViewModel(private val repository: NutriCoachRepo,
    private val foodIntakeRepo: FoodIntakeRepo,
    private val patientRepo: PatientRepo) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    private val _uiStateForData = MutableLiveData<UiState>(UiState.Initial)
    private val _uiStateExtraData = MutableLiveData<UiState>(UiState.Initial)

    val uiState: LiveData<UiState> = _uiState
    val uiStateForData: LiveData<UiState> = _uiStateForData
    val uiStateExtraData: LiveData<UiState> = _uiStateExtraData

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_KEY
    )

    // Logic for storing tips in database

    private val _tips = MutableStateFlow<List<NutriCoachTip>>(emptyList())
    val tips: StateFlow<List<NutriCoachTip>> = _tips

    fun storeTip(tip: String, patientId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (patientId == null) {
                return@launch
            }
            repository.getTipsForPatient(patientId).firstOrNull()?.let { tips ->
                val alreadyExists =
                    tips.any { it.tipText.trim().equals(tip.trim(), ignoreCase = true) }
                if (!alreadyExists) {
                    val generatedTip = NutriCoachTip(tipText = tip, patientOwnerId = patientId)
                    repository.insertTip(generatedTip)
                }
            }
        }
    }

    // Logic for loading saved tips from database

    private val _listOfTips = MutableStateFlow<List<NutriCoachTip>>(emptyList())
    val listOfTips: StateFlow<List<NutriCoachTip>> = _listOfTips

    private val _fetchTipsStatus = MutableLiveData<String>("")
    val fetchTipsStatus: LiveData<String> = _fetchTipsStatus

    fun loadTips(patientId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (patientId == null) {
                return@launch
            }
            val tips = repository.getTipsForPatient(patientId).first()
            if (true) {
                _listOfTips.value = tips
            } else {
                _fetchTipsStatus.postValue("No tips found, try generating some and check back later...")
            }
        }
    }

    private val _deleteTipStatus = MutableLiveData<String>("")
    val deleteTipStatus: LiveData<String> = _deleteTipStatus

    fun resetDeleteStatus() {
        _deleteTipStatus.postValue("")
    }

    // Logic for deleting saved tips from database
    fun deleteAllTipsByPatientId(patientId: Int?) {
        viewModelScope.launch {
            if (patientId == null) {
                return@launch
            }
            repository.clearAllTipsByPatientId(patientId)
            _deleteTipStatus.postValue("Tips have been deleted successfully")
        }
    }

    // Logic for sending prompt to AI
    fun sendPrompt() {
        _uiState.postValue(UiState.Loading)
        val prompt = "Write a short, friendly message that encourages someone to eat more fruit." +
                "Each message should be unique, simple, motivating, and easy to relate to—like a thoughtful nudge from a friend." +
                "Avoid formatting like bold or italics." +
                "Include a creative tip on how to stay healthy while enjoying daily activities like gaming, studying, working, reading, or making art. " +
                "Change the daily activity talked about after each generation, it can be anything, not just the activities I have listed." +
                "Feel free to use light humor, clever wordplay, or vivid imagery to make it fun. " +
                "Keep it concise and send only one message at a time." +
                "Ensure formatting of text is proper, short paragraphs are used with spacing between each paragraph." +
                "You can use emojis as well."
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                val output = response.text
                if (output != null) {
                    _uiState.postValue(UiState.Success(output))
                } else {
                    _uiState.postValue(UiState.Error("Empty response"))
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }

    // Logic for sending prompt for data pattern
    fun sendPromptForPattern() {
        _uiStateForData.postValue(UiState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = foodIntakeRepo.getAllFoodIntake().first()
                val patientList = patientRepo.getAllPatients().first()

                val prompt = foodList.joinToString("\n\n") { intake ->
                    val patient = patientList.find { it.userId == intake.patientOwnerId }
                    if (patient != null) {
                        """
                        Patient: (Sex: ${patient.sex})
                        [Persona]
                        - ${intake.persona}
                
                        [Food Intake]
                        - Fruits: ${intake.fruits}, Vegetables: ${intake.vegetables}, Grains: ${intake.grains}
                        - Red Meat: ${intake.redMeat}, Seafood: ${intake.seaFood}, Poultry: ${intake.poultry}
                        - Fish: ${intake.fish}, Eggs: ${intake.eggs}, Nuts/Seeds: ${intake.nutsSeeds}
                
                        [Timings]
                        - Wake-up: ${intake.wakeUpTime}, Sleep: ${intake.sleepTime}, Big Meal: ${intake.bigMealTime}
                
                        [HEIFA Scores]
                        - Male Total: ${patient.heifaTotalScoreMale}, Female Total: ${patient.heifaTotalScoreFemale}
                        - Vegetables (M/F): ${patient.vegetablesHeifaScoreMale}/${patient.vegetablesHeifaScoreFemale}
                        - Fruits (M/F): ${patient.fruitHeifaScoreMale}/${patient.fruitHeifaScoreFemale}
                        - Dairy (M/F): ${patient.dairyAndAlternativesHeifaScoreMale}/${patient.dairyAndAlternativesHeifaScoreFemale}
                        - Alcohol (M/F): ${patient.alcoholHeifaScoreMale}/${patient.alcoholHeifaScoreFemale}
                        - Water Intake: ${patient.water} L (${patient.waterTotalML} ml)
                        - Sugar Score (M/F): ${patient.sugarHeifaScoreMale}/${patient.sugarHeifaScoreFemale}
                        - Saturated Fat Score (M/F): ${patient.saturatedFatHeifaScoreMale}/${patient.saturatedFatHeifaScoreFemale}
                
                        [Grains & Meat]
                        - Whole Grains: ${patient.wholeGrainsServeSize} serves
                        - Discretionary Serve Size: ${patient.discretionaryServeSize}
                        - Meat Score (M/F): ${patient.meatAndAlternativesHeifaScoreMale}/${patient.meatAndAlternativesHeifaScoreFemale}
                
                        -----------------------------------------
                        Use this information about patients and their food intake data, make logical observation and
                        return three key data patterns. Make sure to present these insights in a clear, structured list
                        Don't provide analysis for each patient separately, analysis must be done based on overall patient's data then compiled into 3 key points
                        
                        Don't use bold formatting, if you want to use convert it to a string so that it can be used natively with kotlin without the **.
                        Ensure paragraph formatting is proper.
                        Remove the 'Here are the three key data patterns' and any other similar boilerplate text
                        Keep it concise and easy to read and understand as if the person reading it cannot see the values.
                        
                       
                        Label the top part as Data Analysis Pattern
                        """.trimIndent()
                    } else {
                        "FoodIntake has no linked patient"
                    }
                }
                val response = generativeModel.generateContent( content { text(prompt) }
                )
                val output = response.text
                if (output != null) {
                    _uiStateForData.postValue(UiState.Success(output))
                } else {
                    _uiStateForData.postValue(UiState.Error("Empty response"))
                }
            } catch (e: Exception) {
                _uiStateForData.postValue(UiState.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }

    //prompt logic for extra analysis
    fun sendPromptForExtraPattern() {
        _uiStateExtraData.postValue(UiState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = foodIntakeRepo.getAllFoodIntake().first()
                val patientList = patientRepo.getAllPatients().first()

                val prompt = foodList.joinToString("\n\n") { intake ->
                    val patient = patientList.find { it.userId == intake.patientOwnerId }
                    if (patient != null) {
                        """
                        Patient: (Sex: ${patient.sex})
                        [Persona]
                        - ${intake.persona}
                
                        [Food Intake]
                        - Fruits: ${intake.fruits}, Vegetables: ${intake.vegetables}, Grains: ${intake.grains}
                        - Red Meat: ${intake.redMeat}, Seafood: ${intake.seaFood}, Poultry: ${intake.poultry}
                        - Fish: ${intake.fish}, Eggs: ${intake.eggs}, Nuts/Seeds: ${intake.nutsSeeds}
                
                        [Timings]
                        - Wake-up: ${intake.wakeUpTime}, Sleep: ${intake.sleepTime}, Big Meal: ${intake.bigMealTime}
                
                        [HEIFA Scores]
                        - Male Total: ${patient.heifaTotalScoreMale}, Female Total: ${patient.heifaTotalScoreFemale}
                        - Vegetables (M/F): ${patient.vegetablesHeifaScoreMale}/${patient.vegetablesHeifaScoreFemale}
                        - Fruits (M/F): ${patient.fruitHeifaScoreMale}/${patient.fruitHeifaScoreFemale}
                        - Dairy (M/F): ${patient.dairyAndAlternativesHeifaScoreMale}/${patient.dairyAndAlternativesHeifaScoreFemale}
                        - Alcohol (M/F): ${patient.alcoholHeifaScoreMale}/${patient.alcoholHeifaScoreFemale}
                        - Water (M/F): ${patient.waterHeifaScoreMale}/${patient.waterHeifaScoreFemale}
                        - Water Intake: ${patient.water} L (${patient.waterTotalML} ml)
                        - Sugar Score (M/F): ${patient.sugarHeifaScoreMale}/${patient.sugarHeifaScoreFemale}
                        - Saturated Fat Score (M/F): ${patient.saturatedFatHeifaScoreMale}/${patient.saturatedFatHeifaScoreFemale}
                                 
                        [Grains & Meat]
                        - Whole Grains: ${patient.wholeGrainsServeSize} serves
                        - Discretionary Serve Size: ${patient.discretionaryServeSize}
                        - Meat Score (M/F): ${patient.meatAndAlternativesHeifaScoreMale}/${patient.meatAndAlternativesHeifaScoreFemale}
                        
                        [EXTRA INFO (TO BE USED FOR FURTHER ANALYSIS)]
                        [Vegetables]
                        - Vegetable with legumes allocated serve size: ${patient.vegetablesWithLegumesAllocatedServeSize}
                        - Legumes allocated vegetables: ${patient.legumesAllocatedVegetables}
                        - Vegetable variation score: ${patient.vegetablesVariationsScore}
                        - Legumes: ${patient.legumes}
                        
                        [Fruits]
                        - Fruit serve size: ${patient.fruitServeSize}
                        - Fruit variation score: ${patient.fruitVariationsScore}
                        - Fruit tropical and subtropical: ${patient.fruitTropicalAndSubtropical}
                                               
                        [Grains and cereals]
                        - Grains and cereals serve size: ${patient.grainsAndCerealsServeSize}
                        - Grains and cereals non whole grains: ${patient.grainsAndCerealsNonWholeGrains}
                        
                        [Meat and alternatives]
                        - Meat and alternatives with legumes allocated serve size: ${patient.meatAndAlternativesWithLegumesAllocatedServeSize}
                        - Legumes allocated meat and alternatives: ${patient.legumesAllocatedMeatAndAlternatives}
                        
                        [Dairy and alternatives]
                        - Dairy and alternatives serve size: ${patient.dairyAndAlternativesServeSize}
                        
                        [Alcohol]
                        - Alcohol standard drinks: ${patient.alcoholStandardDrinks}
                                             
                        [Water]
                        - Water: ${patient.water}
                        - Beverage Total ML: ${patient.beverageTotalML}
                        
                        [Sugar]
                        - Sugar: ${patient.sugar}
                        
                        [Saturated fat]
                        - Saturated fat: ${patient.saturatedFat}
                        
                        [Unsaturated fat]
                        - Unsaturated fat: ${patient.unsaturatedFatServeSize}
                
                        -----------------------------------------
                        Use this information about patients and their food intake data, make logical observation and
                        return three key data patterns. Make sure to present these insights in a clear, structured list
                        Don't provide analysis for each patient separately, analysis must be done based on overall patient's data then compiled into 3 key points
                        
                        Don't use bold formatting, if you want to use convert it to a string so that it can be used natively with kotlin without the **.
                        Ensure paragraph formatting is proper, Use double newlines to separate paragraphs
                        Remove the 'Here are the three key data patterns' and any other similar boilerplate text
         
                        There is extra information present for each patient, 
                        carefully make an observation about the extra info and come up with an analysis which should link to all the categories mentioned with proper reasoning
                          
                        Keep it concise and easy to read and understand, as if the person reading it cannot see the values and don't make it too long
                        Include an additional section to show the reader in general what the deeper analysis includes and how it helps the clinician understand in deeper detail about the state of patients, don't make it too long
                        Label the top part as Deep Analysis Pattern
                        """.trimIndent()
                    } else {
                        "FoodIntake has no linked patient"
                    }
                }
                val response = generativeModel.generateContent( content { text(prompt) }
                )
                val output = response.text
                if (output != null) {
                    _uiStateExtraData.postValue(UiState.Success(output))
                } else {
                    _uiStateExtraData.postValue(UiState.Error("Empty response"))
                }
            } catch (e: Exception) {
                _uiStateExtraData.postValue(UiState.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }

    //getting average heifaScores
    private val _maleAverageScore = MutableLiveData<Float>()
    val maleAverageScore: MutableLiveData<Float> = _maleAverageScore

    private val _femaleAverageScore = MutableLiveData<Float>()
    val femaleAverageScore: MutableLiveData<Float> = _femaleAverageScore

    fun setMaleAverageScore(score: Float) {
        _maleAverageScore.postValue(score)
    }

    fun setFemaleAverageScore(score: Float) {
        _femaleAverageScore.postValue(score)
    }

    fun getAverageHEIFAScore() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = foodIntakeRepo.getAllFoodIntake().first()
                val patientList = patientRepo.getAllPatients().first()

                val heifaScores = foodList.mapNotNull { intake ->
                    patientList.find { it.userId == intake.patientOwnerId}
                }
                val maleScores = heifaScores.mapNotNull { it.heifaTotalScoreMale?.toDouble() }
                val femaleScores = heifaScores.mapNotNull { it.heifaTotalScoreFemale?.toDouble() }

                val averageMale = if (maleScores.isNotEmpty()) maleScores.average().toFloat() else 0f
                val averageFemale = if (femaleScores.isNotEmpty()) femaleScores.average().toFloat() else 0f

                setMaleAverageScore(averageMale)
                setFemaleAverageScore(averageFemale)

            } catch (e: Exception) {
               ""
            }
        }
    }

    //getting optimal fruit score
    private val _optimalFruitScore = MutableLiveData<Boolean>()
    val optimalFruitScore: MutableLiveData<Boolean> = _optimalFruitScore

    suspend fun getOptimalFruitScore(userId: Int) {
        val fruitInfo = patientRepo.getFruitScoreInfo(userId)
        val optimalServeScore = 2.0
        val optimalFruitScore = 5.0

        if (fruitInfo.fruitVariationsScore!! >= optimalFruitScore && fruitInfo.fruitServeSize!! >= optimalServeScore) {
            _optimalFruitScore.value = true
        }
    }
}