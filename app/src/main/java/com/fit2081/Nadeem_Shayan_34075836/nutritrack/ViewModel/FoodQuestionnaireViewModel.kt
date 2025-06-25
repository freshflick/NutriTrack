package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FoodIntake.FoodIntake
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.FoodIntakeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodQuestionnaireViewModel(private val repository: FoodIntakeRepo) : ViewModel() {
    val fruits = MutableLiveData<Boolean>()
    val vegetables = MutableLiveData<Boolean>()
    val grains = MutableLiveData<Boolean>()
    val redMeat = MutableLiveData<Boolean>()
    val seaFood = MutableLiveData<Boolean>()
    val poultry = MutableLiveData<Boolean>()
    val fish = MutableLiveData<Boolean>()
    val eggs = MutableLiveData<Boolean>()
    val nutsSeeds = MutableLiveData<Boolean>()

    val persona = MutableLiveData<String>()

    val foodCategoryMap = mapOf(
        "Fruits" to fruits,
        "Vegetables" to vegetables,
        "Grains" to grains,
        "Red meat" to redMeat,
        "Seafood" to seaFood,
        "Poultry" to poultry,
        "Fish" to fish,
        "Eggs" to eggs,
        "Nuts/Seeds" to nutsSeeds
    )

    private val _bigMealTime = MutableLiveData("00:00")
    val bigMealTime: LiveData<String> = _bigMealTime

    fun setBigMealTime(time: String) {
        _bigMealTime.value = time
    }

    private val _sleepTime = MutableLiveData("00:00")
    val sleepTime: LiveData<String> = _sleepTime

    fun setSleepTime(time: String) {
        _sleepTime.value = time
    }

    private val _wakeUpTime = MutableLiveData("00:00")
    val wakeUpTime: LiveData<String> = _wakeUpTime

    fun setWakeUpTime(time: String) {
        _wakeUpTime.value = time
    }

    fun setPersona(value: String) {
        persona.value = value
    }

    fun loadCheckboxSelections(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId == null) return@launch

            Log.d("FoodViewModel", "ViewModel instance: ${this.hashCode()}")

            val savedSelections = repository.getFoodIntakeByPatientItems(userId)
            Log.d("FoodLoad", "Loaded intake: $savedSelections")

            if (savedSelections != null) {
                fruits.postValue(savedSelections.fruits)
                vegetables.postValue(savedSelections.vegetables)
                grains.postValue(savedSelections.grains)
                redMeat.postValue(savedSelections.redMeat)
                seaFood.postValue(savedSelections.seaFood)
                poultry.postValue(savedSelections.poultry)
                fish.postValue(savedSelections.fish)
                eggs.postValue(savedSelections.eggs)
                nutsSeeds.postValue(savedSelections.nutsSeeds)
                persona.postValue(savedSelections.persona)
                _bigMealTime.postValue(savedSelections.bigMealTime)
                _sleepTime.postValue(savedSelections.sleepTime)
                _wakeUpTime.postValue(savedSelections.wakeUpTime)
            } else {
                Log.d("FoodLoad", "No intake data found for user $userId – skipping load.")
            }
        }
    }

    // Food category logic --------------------------------------------

    //if no option true, prompt user to select at least one category
    private val _categoryStatus = MutableLiveData<String?>()
    val categoryStatus: LiveData<String?> = _categoryStatus

    fun addIntake(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("FoodIntake", "are u null: $userId")
            if (userId == null) return@launch
            Log.d("FoodIntake", "addIntake called for user: $userId")
           val intake = FoodIntake(
               patientOwnerId = userId,
               fruits = fruits.value == true,
               vegetables = vegetables.value == true,
               grains = grains.value == true,
               redMeat = redMeat.value == true,
               seaFood = seaFood.value == true,
               poultry = poultry.value == true,
               fish = fish.value == true,
               eggs = eggs.value == true ,
               nutsSeeds = nutsSeeds.value == true,
               persona = persona.value ?: "",
               bigMealTime = bigMealTime.value ?: "",
               sleepTime = sleepTime.value ?: "",
               wakeUpTime = wakeUpTime.value ?: ""
           )
            repository.insertFoodIntake(intake)
        }
    }
}