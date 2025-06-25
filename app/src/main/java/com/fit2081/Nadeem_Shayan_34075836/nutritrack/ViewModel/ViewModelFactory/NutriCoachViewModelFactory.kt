package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.NutriCoachViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.NutriCoachRepo

class NutriCoachViewModelFactory(
    private val repository: NutriCoachRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutriCoachViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutriCoachViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}