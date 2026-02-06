package com.example.interprac.ui.state

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val token: String, val isAdmin: Boolean, val username: String) : AuthState()
    data class Error(val message: String) : AuthState()
    object Unauthenticated : AuthState()
}

