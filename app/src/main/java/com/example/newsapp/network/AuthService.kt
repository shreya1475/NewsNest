package com.example.newsapp.network

import com.example.newsapp.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("wp-json/jwt-auth/v1/token")
    suspend fun login(@Body credentials: Map<String, String>): Response<LoginResponse>
}
