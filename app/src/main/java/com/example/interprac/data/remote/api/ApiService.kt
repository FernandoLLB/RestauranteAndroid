package com.example.interprac.data.remote.api

import com.example.interprac.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth endpoints
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    // Admin endpoints (require Bearer token)
    @GET("api/admin/users")
    suspend fun getAllUsers(@Header("Authorization") token: String): Response<List<UserDto>>

    @PUT("api/admin/users/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Long,
        @Body request: UpdateUserRequest
    ): Response<Unit>

    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Long
    ): Response<Unit>
}

