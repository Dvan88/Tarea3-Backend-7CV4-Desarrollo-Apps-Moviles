package com.example.android

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("/")
    suspend fun checkApi(): Response<ApiCheckResponse>
}