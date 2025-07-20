package com.example.newsapp.reader

data class Post(
    val id: Int,
    val title: Rendered,
    val content: Rendered,
    val excerpt: Rendered,
    val link: String,
    val date: String,
    val featured_media: Int,
    var imageUrl: String? = null // will be filled later
)

data class Rendered(
    val rendered: String
)

data class PostRequest(
    val title: String,
    val content: String,
    val status: String = "publish"
)

