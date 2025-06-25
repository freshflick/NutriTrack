package com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.AuthManager
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterAndLoginScreenViewModel(private val repository: PatientRepo,
    private val dataStoreRepo: DataStoreRepo) : ViewModel() {
    val userId = MutableLiveData<Int>()
    val phoneNumber = MutableLiveData<Long>()
    val name = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    // Login screen logic -------------------------------------------

    private val _loginStatus = MutableLiveData<String?>()
    val loginStatus: LiveData<String?> = _loginStatus

    private val _allUserIds = MutableStateFlow<List<Int>>(emptyList())
    val allUserIds: StateFlow<List<Int>> = _allUserIds

    fun clearLoginStatus() {
        _loginStatus.value = null
    }

    fun validateLogin(selectedUserId: Int?, passwordInput: String) {
        val passwordInput = passwordInput

        if (selectedUserId == null) {
            _loginStatus.postValue("User ID is not selected")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val ids = repository.getAllPatientIds().first()
            _allUserIds.value = ids

            val patient = repository.getPatientByUserId(selectedUserId)
            val storedPassword = patient?.password

            if (storedPassword == null) {
                _loginStatus.postValue("User not found.")
            } else if (passwordInput == storedPassword) {
                val success = repository.login(selectedUserId, passwordInput)

                //use datastore store
                dataStoreRepo.saveUserId(selectedUserId)
                dataStoreRepo.savePassword(passwordInput)
                dataStoreRepo.setLoginState(true)

                _loginStatus.postValue(
                    if (success) "Successfully logged in"
                    else "Incorrect credentials"
                )
            } else {
                _loginStatus.postValue("Incorrect password")
            }
        }
    }

    fun fetchUserIds() {
        viewModelScope.launch {
            repository.getAllPatientIds().collect { ids ->
                _allUserIds.value = ids
            }
        }
    }

    fun checkLoggedIn(): Flow<Boolean> {
        return dataStoreRepo.getLoginState()
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreRepo.logout()
        }
    }

    suspend fun persistLogin() {
        val userId = dataStoreRepo.readUserId().first()
        val password = dataStoreRepo.readPassword()
        val isLoggedIn = dataStoreRepo.getLoginState()

        AuthManager.login(userId)
    }


    // Register screen logic --------------------------------------------

    private val _registrationStatus = MutableLiveData<String?>()
    val registrationLiveStatus: LiveData<String?> = _registrationStatus

    fun clearRegistrationStatus() {
        _registrationStatus.value = null
    }

    private val _passwordStatus = MutableLiveData<String?>()
    val passwordStatus: LiveData<String?> = _passwordStatus

    fun clearPasswordStatus() {
        _passwordStatus.value = null
    }

    val registerResult = MutableStateFlow<String?>(null)

    fun registeredUser() {
        val id = userId.value
        val phone = phoneNumber.value
        val newName = name.value
        val newPassword = password.value
        val confirm = confirmPassword.value

        //validation and extra security check
        if (id == null || newName.isNullOrBlank() || newPassword.isNullOrBlank() || confirm.isNullOrBlank()) {
            _registrationStatus.value = "Please fill in all the fields."
            return
        }

        if (!newPassword.any{ it.isDigit()} && !newPassword.any{ !it.isLetterOrDigit() }) {
            _passwordStatus.value = "Password must contain at least one digit and one special characters"
            return
        }

        _passwordStatus.value = "Valid password"

        if (newPassword.length !in 6..12) {
            _passwordStatus.value = "Password must be between 6 and 12 characters long"
            return
        }

        if (newPassword != confirm) {
            _registrationStatus.value = "Passwords don't match."
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val ids = repository.getAllPatientIds().first()
            Log.d("Database", "Fetched user ids: $ids")
            _allUserIds.value = ids

            val patient = repository.getPatientByUserId(id)
            if (patient != null) {
                val storedPhone = patient.phoneNumber
                if (storedPhone == phone) {
                    repository.updateBasicInfo(id, newName, newPassword)
                    _registrationStatus.postValue("Registration successful, please login with your registered details.")
                } else {
                    _registrationStatus.postValue("Phone number does not match our records.")
                }
            } else {
                _registrationStatus.postValue("User ID not found.")
            }
        }
    }
}