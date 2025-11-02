package com.example.newsapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostUploadRetrofit {
    val apiService: PostUploadApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://blogpost.infinityfreeapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostUploadApiService::class.java)
    }
}
