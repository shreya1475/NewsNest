package com.example.newsapp.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.awaitResponse
import java.io.File
import java.io.FileOutputStream
import com.example.newsapp.model.WPCreatePost
import com.example.newsapp.network.PostUploadRetrofit

suspend fun uploadPostToWordPress(
    context: Context,
    username: String,
    appPassword: String,
    title: String,
    content: String,
    imageUri: Uri?,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    try {
        val authHeader = createBasicAuthHeader(username, appPassword)
        var imageId: Int? = null

        // Upload Image if selected
        if (imageUri != null) {
            val imageFile = createTempFileFromUri(context, imageUri)
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
            val titlePart = RequestBody.create("text/plain".toMediaTypeOrNull(), "Featured Image")

            val mediaResponse = PostUploadRetrofit.apiService
                .uploadImage(authHeader, imagePart, titlePart)
                .awaitResponse()

            if (!mediaResponse.isSuccessful) {
                onError("Image upload failed: ${mediaResponse.errorBody()?.string()}")
                return
            }

            imageId = mediaResponse.body()?.id
        }

        // Create post
        val post = WPCreatePost(title = title, content = content, featured_media = imageId)
        val postResponse = PostUploadRetrofit.apiService
            .createPost(authHeader, post)
            .awaitResponse()

        if (postResponse.isSuccessful) {
            onSuccess("Posted successfully: ${postResponse.body()?.link}")
        } else {
            onError("Post failed: ${postResponse.errorBody()?.string()}")
        }

    } catch (e: Exception) {
        onError("Error: ${e.message}")
    }
}

private fun createBasicAuthHeader(username: String, appPassword: String): String {
    val credentials = "$username:$appPassword"
    val encoded = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    return "Basic $encoded"
}

private suspend fun createTempFileFromUri(context: Context, uri: Uri): File = withContext(Dispatchers.IO) {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: 0
    cursor?.moveToFirst()
    val name = cursor?.getString(nameIndex) ?: "temp_image.jpg"
    cursor?.close()

    val file = File(context.cacheDir, name)
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output -> input.copyTo(output) }
    }
    file
}
