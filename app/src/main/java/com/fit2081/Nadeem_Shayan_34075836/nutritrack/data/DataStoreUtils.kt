package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// to store login info persistence
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Login")

object PreferenceKeys {
    val userId = intPreferencesKey("userId")
    val password = stringPreferencesKey("password")
    val isLoggedIn = booleanPreferencesKey("is_logged_in")
    val lastScreenKey = stringPreferencesKey("last_screen")
    val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
}