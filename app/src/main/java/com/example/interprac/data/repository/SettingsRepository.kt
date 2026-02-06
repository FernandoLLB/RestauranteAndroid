package com.example.interprac.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.interprac.data.local.PreferencesKeys
import com.example.interprac.data.local.dataStore
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    fun observeDarkMode() = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_MODE] ?: false
    }

    suspend fun setDarkMode(enable: Boolean){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = enable
        }
    }
}