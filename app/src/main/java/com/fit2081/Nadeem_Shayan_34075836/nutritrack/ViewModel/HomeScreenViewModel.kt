package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: PatientRepo) : ViewModel() {
    val name = MutableLiveData<String>()
    val heifaTotalScore = MutableLiveData<Double?>()

    // Home screen logic --------------------------------------------
    fun loadHomeScreenInfo(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId == null) return@launch

            val savedSelections = repository.getHomeScreenInfo(userId)
            if (savedSelections != null) {
                name.postValue(savedSelections.name)
                if (savedSelections.sex.uppercase() == "MALE") {
                    heifaTotalScore.postValue(savedSelections.heifaTotalScoreMale)
                }
                else {
                    heifaTotalScore.postValue(savedSelections.heifaTotalScoreFemale)
                }
            }
            else {
                Log.d("InsightLoad", "No insight data found for user $userId – skipping load.")
            }
        }
    }
}