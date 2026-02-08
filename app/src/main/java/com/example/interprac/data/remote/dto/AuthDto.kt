package com.example.interprac.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

data class AuthResponse(
    val token: String
)

data class UserDto(
    val id: Long,
    val username: String,
    val firstname: String? = null,
    val lastname: String? = null,
    val role: String = "USER"
)

data class UpdateUserRequest(
    val firstname: String,
    val lastname: String,
    val role: String
)
