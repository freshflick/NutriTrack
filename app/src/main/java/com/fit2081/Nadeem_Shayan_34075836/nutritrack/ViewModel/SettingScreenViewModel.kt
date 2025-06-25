package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingScreenViewModel(private val repository: PatientRepo,
                             private val dataStoreRepo: DataStoreRepo) : ViewModel() {
    val name = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<Long>()
    val userid = MutableLiveData<Int>()

    val isDarkMode = dataStoreRepo.isDarkModeFlow
        .asLiveData(viewModelScope.coroutineContext)

    // Home screen logic --------------------------------------------
    fun loadSettingsInfo(userId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userId == null) return@launch

            val savedSelections = repository.getBasicInfo(userId)
            name.postValue(savedSelections.name)
            phoneNumber.postValue(savedSelections.phoneNumber)
            userid.postValue(savedSelections.userId)
        }
    }

    //dark mode logic
    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepo.setDarkMode(enabled)
        }
    }
}