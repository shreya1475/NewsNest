package com.example.newsapp.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val fetchedPosts = RetrofitClient.apiService.getPosts()
                val postsWithImages = fetchedPosts.map { post ->
                    if (post.featured_media != 0) {
                        try {
                            val media = RetrofitClient.apiService.getMedia(post.featured_media)
                            post.copy(imageUrl = media.source_url)
                        } catch (_: Exception) {
                            post
                        }
                    } else post
                }
                _posts.value = postsWithImages
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

