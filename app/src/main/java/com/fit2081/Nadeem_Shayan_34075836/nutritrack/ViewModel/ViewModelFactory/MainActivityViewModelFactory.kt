package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.MainActivityViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo

class MainActivityViewModelFactory(
    private val application: Application,
    private val repository: PatientRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}