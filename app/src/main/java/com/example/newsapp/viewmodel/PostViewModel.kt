package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.WordPressPost
import com.example.newsapp.network.WordPressApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostsViewModel(token: String, baseUrl: String) : ViewModel() {
    private val api = WordPressApiService.create(baseUrl, token)

    private val _posts = MutableStateFlow<List<WordPressPost>>(emptyList())
    val posts: StateFlow<List<WordPressPost>> = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                val data = api.getPosts()
                _posts.value = data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getAuthorName(authorId: Int): String {
        return try {
            api.getAuthor(authorId).name
        } catch (e: Exception) {
            "Unknown Author"
        }
    }

    suspend fun getFeaturedImageUrl(mediaId: Int): String? {
        return try {
            if (mediaId == 0) null else api.getMedia(mediaId).source_url
        } catch (e: Exception) {
            null
        }
    }
}
