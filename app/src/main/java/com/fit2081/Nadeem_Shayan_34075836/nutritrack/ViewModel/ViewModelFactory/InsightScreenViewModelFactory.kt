package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.InsightViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.InsightsRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo

class InsightScreenViewModelFactory(
    private val repository: InsightsRepo,
    private val patientRepo: PatientRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InsightViewModel(repository, patientRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}