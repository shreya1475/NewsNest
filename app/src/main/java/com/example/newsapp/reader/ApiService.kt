package com.example.newsapp.reader


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("media/{id}")
    suspend fun getMedia(@Path("id") id: Int): Media

    @POST("posts")
    @Headers("Content-Type: application/json")
    fun createPost(@Body post: PostRequest): Call<Unit>
}



