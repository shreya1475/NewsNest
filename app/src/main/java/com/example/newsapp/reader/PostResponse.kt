package com.example.newsapp.reader

data class PostResponse(
    val found: Int,
    val posts: List<Post>
)
