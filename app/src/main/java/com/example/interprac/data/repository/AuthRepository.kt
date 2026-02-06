package com.example.interprac.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.interprac.data.local.PreferencesKeys
import com.example.interprac.data.local.dataStore
import com.example.interprac.data.remote.RetrofitClient
import com.example.interprac.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthRepository(private val context: Context) {

    private val apiService = RetrofitClient.apiService

    fun observeToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTH_TOKEN]
    }

    fun observeUsername(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME]
    }

    fun observeIsAdmin(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_ADMIN] ?: false
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[PreferencesKeys.AUTH_TOKEN]
    }

    suspend fun getUsername(): String? {
        return context.dataStore.data.first()[PreferencesKeys.USER_NAME]
    }

    suspend fun isAdmin(): Boolean {
        return context.dataStore.data.first()[PreferencesKeys.IS_ADMIN] ?: false
    }

    suspend fun saveSession(token: String, username: String, isAdmin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN] = token
            preferences[PreferencesKeys.USER_NAME] = username
            preferences[PreferencesKeys.IS_ADMIN] = isAdmin
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.AUTH_TOKEN)
            preferences.remove(PreferencesKeys.USER_NAME)
            preferences.remove(PreferencesKeys.IS_ADMIN)
        }
    }

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun register(
        email: String,
        password: String,
        firstname: String,
        lastname: String
    ): Result<Unit> {
        return try {
            val response = apiService.register(
                RegisterRequest(email, password, firstname, lastname)
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error de registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(): Result<List<UserDto>> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("No hay token"))
            val response = apiService.getAllUsers("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(userId: Long, firstname: String, lastname: String, role: String): Result<Unit> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("No hay token"))
            val response = apiService.updateUser(
                "Bearer $token",
                userId,
                UpdateUserRequest(firstname, lastname, role)
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: Long): Result<Unit> {
        return try {
            val token = getToken() ?: return Result.failure(Exception("No hay token"))
            val response = apiService.deleteUser("Bearer $token", userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
