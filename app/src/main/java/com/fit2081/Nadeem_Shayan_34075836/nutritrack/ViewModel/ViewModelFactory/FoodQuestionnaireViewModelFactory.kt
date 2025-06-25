package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.FoodQuestionnaireViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.FoodIntakeRepo

class FoodQuestionnaireViewModelFactory(
    private val repository: FoodIntakeRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodQuestionnaireViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodQuestionnaireViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}