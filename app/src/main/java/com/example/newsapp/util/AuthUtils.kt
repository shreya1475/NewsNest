package com.example.newsapp.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.newsapp.network.WordPressAuthService
import com.example.newsapp.repository.AuthRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun formatUrl(userInput: String): String {
    var url = userInput.trim()
    if (!url.startsWith("http")) {
        url = "https://$url"
    }
    if (!url.endsWith("/")) {
        url += "/"
    }
    return url
}

fun createAuthService(baseUrl: String): WordPressAuthService {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(WordPressAuthService::class.java)
}

fun saveToken(context: Context, token: String) {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    sharedPrefs.edit().putString("jwt_token", token).apply()
}

fun loginUser(
    context: Context,
    baseUrl: String,
    username: String,
    password: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val result = AuthRepository.login(baseUrl, username, password)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    val token = result.getOrThrow()
                    onSuccess(token)
                } else {
                    onError(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }
}
