package com.example.newsapp.repository

import com.example.newsapp.model.LoginResponse
import com.example.newsapp.network.AuthService
import com.example.newsapp.network.RetrofitInstance
import retrofit2.HttpException

object AuthRepository {

    suspend fun login(
        baseUrl: String,
        username: String,
        password: String
    ): Result<String> {
        return try {
            val service = RetrofitInstance.getAuthService(baseUrl)
            val response = service.login(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse?.token != null) {
                    Result.success(loginResponse.token)
                } else {
                    Result.failure(Exception("Login succeeded but token is null."))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Login failed."))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
