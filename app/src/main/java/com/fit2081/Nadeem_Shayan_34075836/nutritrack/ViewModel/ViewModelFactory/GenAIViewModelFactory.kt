package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.GenAIViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.FoodIntakeRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.NutriCoachRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo

class GenAIViewModelFactory(
    private val repository: NutriCoachRepo,
    private val foodIntakeRepo: FoodIntakeRepo,
    private val patientRepo: PatientRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenAIViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GenAIViewModel(repository, foodIntakeRepo, patientRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}