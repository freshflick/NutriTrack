package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Patient.PatientDao

object AuthManager {
    // Internal user ID state that tracks the currently logged-in user's ID
    private val _userId: MutableState<Int?> = mutableStateOf(null)

    // Function to log in a user by setting their user ID
    fun login(userId: Int) {
        _userId.value = userId
    }

    // Function to log out the user by clearing the user ID
    fun logout() {
        _userId.value = null
    }

    private var loginSuccessCallback: (() -> Unit)? = null

    // Method to set the callback for login success
    fun setOnLoginSuccessCallback(callback: () -> Unit) {
        loginSuccessCallback = callback
    }

    // Function to retrieve the currently logged-in patient's ID
    fun getPatientId(): Int? {
        return _userId.value
    }

    suspend fun login(patientDao: PatientDao, userId: Int, password: String): Boolean {
        val patient = patientDao.getPatientByUserId(userId)
        return if (patient?.password == password) {
            _userId.value = userId
            loginSuccessCallback?.invoke()
            true
        } else {
            false
        }
    }
}
