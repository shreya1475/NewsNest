package com.example.newsapp.model

data class WPCreatePost(
    val title: String,
    val content: String,
    val status: String = "publish",
    val featured_media: Int? = null
)

data class WPMediaResponse(
    val id: Int
)

data class WPPostResponse(
    val id: Int,
    val link: String
)