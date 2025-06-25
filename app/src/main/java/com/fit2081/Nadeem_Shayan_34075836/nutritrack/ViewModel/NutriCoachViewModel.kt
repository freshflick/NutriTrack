package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.NutriCoachRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NutriCoachViewModel(private val repository: NutriCoachRepo) : ViewModel() {
    // FruityVice API Logic --------------------------------------------
    val fruitName = MutableLiveData<String>()
    val family = MutableLiveData<String>()
    val calories = MutableLiveData<Float>()
    val fat = MutableLiveData<Float>()
    val sugar = MutableLiveData<Float>()
    val carbohydrate = MutableLiveData<Float>()
    val protein = MutableLiveData<Float>()

    fun getFruitInfo(fruit: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (fruit == null) return@launch

            val fruitToSelect = repository.getFruitByName(fruit)
            if (fruitToSelect != null) {
                val nutrition = fruitToSelect.nutritions

                fruitName.postValue(fruitToSelect.name)
                family.postValue(fruitToSelect.family)
                calories.postValue(nutrition.calories)
                fat.postValue(nutrition.fat)
                sugar.postValue(nutrition.sugar)
                carbohydrate.postValue(nutrition.carbohydrates)
                protein.postValue(nutrition.protein)
            }
            else {
                Log.d("InsightLoad", "No data found for $fruit.")
            }
        }
    }

    // GenAI API Logic --------------------------------------------
    val tip = MutableLiveData<String>()
}