package com.example.newsapp.network

import com.example.newsapp.model.WPCreatePost
import com.example.newsapp.model.WPMediaResponse
import com.example.newsapp.model.WPPostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PostUploadApiService {

    @Multipart
    @POST("wp-json/wp/v2/media")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody
    ): Call<WPMediaResponse>

    @POST("wp-json/wp/v2/posts")
    fun createPost(
        @Header("Authorization") token: String,
        @Body post: WPCreatePost
    ): Call<WPPostResponse>
}
