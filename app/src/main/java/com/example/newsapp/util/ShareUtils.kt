package com.example.newsapp.util

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

suspend fun shareToWhatsApp(
    context: android.content.Context,
    title: String,
    content: String,
    url: String,
    image: Bitmap?
) {
    withContext(Dispatchers.IO) {
        val message = "$title\n\n$content\n\nRead more: $url"

        if (image != null) {
            try {
                val cachePath = File(context.cacheDir, "images")
                cachePath.mkdirs()
                val file = File(cachePath, "shared_image.png")
                val stream = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()

                val imageUri: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    putExtra(Intent.EXTRA_TEXT, message)
                    setPackage("com.whatsapp")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                context.startActivity(Intent.createChooser(intent, "Share with"))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show()
            }
        } else {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, message)
                    setPackage("com.whatsapp")
                }

                context.startActivity(Intent.createChooser(intent, "Share with"))
            } catch (e: Exception) {
                Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
