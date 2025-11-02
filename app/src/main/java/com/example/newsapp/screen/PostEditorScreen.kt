package com.example.newsapp.screen

import android.graphics.Rect
import android.net.Uri
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsapp.utils.uploadPostToWordPress
import kotlinx.coroutines.launch

@Composable
fun PostEditorScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current
    val density = LocalDensity.current
    var keyboardHeightDp by remember { mutableStateOf(0.dp) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        selectedImageUri = it
    }

    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keyboardHeightPx = screenHeight - rect.bottom
            keyboardHeightDp = with(density) { keyboardHeightPx.toDp() }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 38.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Handle back */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Row {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val username = "shreya"
                                val appPassword = "MrRL tRNQ ZRkc qmP7 o4O3 Uvxt"

                                uploadPostToWordPress(
                                    context = context,
                                    username = username,
                                    appPassword = appPassword,
                                    title = title,
                                    content = content,
                                    imageUri = selectedImageUri,
                                    onSuccess = { actualPostUrl ->
                                        Toast.makeText(context, "Posted successfully", Toast.LENGTH_LONG).show()

                                        navController.navigate(
                                            "post_share_screen?" +
                                                    "title=${Uri.encode(title)}&" +
                                                    "content=${Uri.encode(content)}&" +
                                                    "imageUri=${selectedImageUri}&" +
                                                    "url=${Uri.encode(actualPostUrl)}"
                                        )
                                    }
                                    ,
                                    onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .width(110.dp)
                    ) {
                        Text("Publish", fontSize = 16.sp)
                    }

                    IconButton(onClick = { /* TODO: Handle more */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                }
            }

            // Title input
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                cursorBrush = SolidColor(Color.White.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text("Add title", color = Color.Gray, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content input
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp, lineHeight = 28.sp),
                cursorBrush = SolidColor(Color.White.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxSize(),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text("Start writing...", color = Color.Gray, fontSize = 18.sp)
                    }
                    innerTextField()
                }
            )
        }

        // Floating Add Image Button (above keyboard)
        FloatingActionButton(
            onClick = { launcher.launch("image/*") },
            containerColor = Color.DarkGray,
            contentColor = Color.White,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = keyboardHeightDp + 16.dp)
        ) {
            Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Image")
        }
    }
}
