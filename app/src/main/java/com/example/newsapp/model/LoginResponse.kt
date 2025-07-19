package com.example.newsapp.model

data class LoginResponse(
    val token: String,
    val userEmail: String,
    val userNicename: String,
    val userDisplayName: String
)
