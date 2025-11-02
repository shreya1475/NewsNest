package com.example.newsapp.reader

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://blogpost.infinityfreeapp.com/wp-json/wp/v2/"

    // Replace with your actual WP username and App Password (no spaces)
    private const val username = "shreya"
    private const val appPassword = "nFjS 32y2 u4hm rQPn 3N0j EFYn"

    // Interceptor to add Authorization header
    private val authInterceptor = Interceptor { chain ->
        val credentials = "$username:$appPassword"
        val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", basicAuth)
            .addHeader("Content-Type", "application/json")
            .build()

        chain.proceed(request)
    }

    // OkHttp client with auth interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // Retrofit instance
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
