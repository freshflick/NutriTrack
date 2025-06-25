package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.RegisterAndLoginScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo

class RegisterAndLoginScreenViewModelFactory(
    private val repository: PatientRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterAndLoginScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterAndLoginScreenViewModel(repository, dataStoreRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}