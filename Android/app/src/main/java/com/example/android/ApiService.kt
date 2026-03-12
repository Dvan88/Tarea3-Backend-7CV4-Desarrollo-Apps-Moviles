package com.example.android

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun checkApi(): Response<ApiResponse>
}