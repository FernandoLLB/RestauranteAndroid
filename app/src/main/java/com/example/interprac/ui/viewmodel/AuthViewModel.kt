package com.example.interprac.ui.viewmodel

import android.content.Context
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprac.data.remote.dto.UserDto
import com.example.interprac.data.repository.AuthRepository
import com.example.interprac.data.uiState.AuthState
import com.example.interprac.data.uiState.UiState
import com.example.interprac.util.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _usersState = MutableStateFlow<UiState<List<UserDto>>>(UiState.Idle)
    val usersState: StateFlow<UiState<List<UserDto>>> = _usersState.asStateFlow()

    var isOnline by mutableStateOf(true)
        private set

    var currentUsername by mutableStateOf<String?>(null)
        private set

    var isAdmin by mutableStateOf(false)
        private set

    init {
        checkExistingSession()
        observeNetworkState()
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            NetworkUtils.observeNetworkState(context).collect { online ->
                isOnline = online
            }
        }
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            val token = authRepository.getToken()
            val username = authRepository.getUsername()
            val admin = authRepository.isAdmin()

            if (token != null && username != null) {
                currentUsername = username
                isAdmin = admin
                _authState.value = AuthState.Authenticated(token, admin, username)
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun login(username: String, password: String) {
        if (!isOnline) {
            _authState.value = AuthState.Error("Sin conexión a internet")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            android.util.Log.d("AuthViewModel", "Intentando login con username: $username")

            authRepository.login(username, password).fold(
                onSuccess = { response ->
                    android.util.Log.d("AuthViewModel", "Login exitoso, token recibido")
                    // Decode JWT to check if admin
                    val isAdminUser = decodeJwtRole(response.token)
                    authRepository.saveSession(response.token, username, isAdminUser)
                    currentUsername = username
                    isAdmin = isAdminUser
                    _authState.value = AuthState.Authenticated(response.token, isAdminUser, username)
                },
                onFailure = { error ->
                    android.util.Log.e("AuthViewModel", "Login fallido: ${error.message}")
                    _authState.value = AuthState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun register(username: String, password: String, firstname: String, lastname: String) {
        if (!isOnline) {
            _authState.value = AuthState.Error("Sin conexión a internet")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            authRepository.register(username, password, firstname, lastname).fold(
                onSuccess = {
                    // After successful registration, login automatically
                    login(username, password)
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.message ?: "Error de registro")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearSession()
            currentUsername = null
            isAdmin = false
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun loadUsers() {
        if (!isOnline) {
            _usersState.value = UiState.Error("Sin conexión a internet")
            return
        }

        viewModelScope.launch {
            _usersState.value = UiState.Loading

            authRepository.getAllUsers().fold(
                onSuccess = { users ->
                    _usersState.value = UiState.Success(users)
                },
                onFailure = { error ->
                    _usersState.value = UiState.Error(error.message ?: "Error al cargar usuarios")
                }
            )
        }
    }

    fun updateUser(userId: Long, firstname: String, lastname: String, role: String) {
        if (!isOnline) {
            _usersState.value = UiState.Error("Sin conexión a internet")
            return
        }

        viewModelScope.launch {
            authRepository.updateUser(userId, firstname, lastname, role).fold(
                onSuccess = { loadUsers() },
                onFailure = { error ->
                    _usersState.value = UiState.Error(error.message ?: "Error al actualizar")
                }
            )
        }
    }

    fun deleteUser(userId: Long) {
        if (!isOnline) {
            _usersState.value = UiState.Error("Sin conexión a internet")
            return
        }

        viewModelScope.launch {
            authRepository.deleteUser(userId).fold(
                onSuccess = { loadUsers() },
                onFailure = { error ->
                    _usersState.value = UiState.Error(error.message ?: "Error al eliminar")
                }
            )
        }
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }

    private fun decodeJwtRole(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                val json = JSONObject(payload)
                val role = json.optString("role", "USER")
                role == "ADMIN"
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}

