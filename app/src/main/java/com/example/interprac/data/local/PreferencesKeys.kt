package com.example.interprac.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val AUTH_TOKEN = stringPreferencesKey("auth_token")
    val USER_NAME = stringPreferencesKey("user_name")
    val IS_ADMIN = booleanPreferencesKey("is_admin")
}