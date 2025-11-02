package com.example.newsapp.network

import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.annotations.SerializedName


interface WordPressAuthService {
    @FormUrlEncoded
    @POST("jwt-auth/v1/token")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): JwtResponse
}

data class JwtResponse(
    val token: String,

    @SerializedName("user_email")
    val userEmail: String,

    @SerializedName("user_nicename")
    val userNicename: String,

    @SerializedName("user_display_name")
    val userDisplayName: String
)
