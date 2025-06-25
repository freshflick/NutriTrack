package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.PreferenceKeys
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.PreferenceKeys.DARK_MODE_KEY
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.PreferenceKeys.lastScreenKey
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepo(private val context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.userId] = userId
        }
    }

    suspend fun savePassword(password: String) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.password] = password
        }
    }

    suspend fun setLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.isLoggedIn] = isLoggedIn
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.isLoggedIn] = false
            prefs.remove(PreferenceKeys.userId)
            prefs.remove(PreferenceKeys.password)
        }
    }

    suspend fun saveLastScreen(screen: String) {
        context.dataStore.edit { prefs -> prefs[lastScreenKey] = screen }
    }

    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[DARK_MODE_KEY] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }

    fun readUserId(): Flow<Int> = dataStore.data
        .map { prefs -> prefs[PreferenceKeys.userId] ?: 0 }

    fun readPassword(): Flow<String> = dataStore.data
        .map { prefs -> prefs[PreferenceKeys.password] ?: "" }

    fun getLoginState(): Flow<Boolean> = dataStore.data
        .map {prefs -> prefs[PreferenceKeys.isLoggedIn] ?: false}

    fun getLastScreen(): Flow<String> = context.dataStore.data
        .map { prefs -> prefs[lastScreenKey] ?: "home" }
}