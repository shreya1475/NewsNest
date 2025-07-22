package com.example.newsapp.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.newsapp.util.shareToWhatsApp
import kotlinx.coroutines.launch
import kotlin.math.round

@Composable
fun PostShareScreen(
    title: String,
    content: String,
    imageUri: Uri?,
    postUrl: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUri)
                .allowHardware(false)
                .build()

            val result = loader.execute(request)
            if (result is SuccessResult) {
                bitmap = result.drawable.toBitmap()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // WhatsApp
            Button(
                onClick = {
                    coroutineScope.launch {
                        shareToWhatsApp(context, title, content, postUrl, bitmap)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25D366),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share to WhatsApp", fontSize = 16.sp)
            }

            // Instagram
            Button(
                onClick = { /* TODO: Implement later */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFEDA77), // Yellow
                                Color(0xFFF58529), // Orange
                                Color(0xFFDD2A7B), // Pink
                                Color(0xFF8134AF), // Purple
                                Color(0xFF515BD4)  // Blue
                            )
                        )
                    )
            ) {
                Text("Share to Instagram", fontSize = 16.sp)
            }

            // Signal
            Button(
                onClick = { /* TODO: Implement later */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A76F0),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share to Signal", fontSize = 16.sp)
            }

            // Email
            Button(
                onClick = { /* TODO: Implement later */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD93025),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Email", fontSize = 16.sp)
            }
        }
    }
}
