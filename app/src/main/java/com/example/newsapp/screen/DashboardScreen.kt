package com.example.newsapp.screen

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.model.WordPressPost
import com.example.newsapp.viewmodel.PostsViewModel
import com.example.newsapp.viewmodel.PostsViewModelFactory

@Composable
fun DashboardScreen(
    token: String,
    baseUrl: String,
    publishedToday: Int = 5,
    totalPosts: Int = 105,
    drafts: Int = 3,
    pending: Int = 11,
    newComments: Int = 201,
    spam: Int = 25,
    navController: NavHostController,
    onAnalyticsClick: () -> Unit = {},
    onManageClick: () -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    val factory = remember { PostsViewModelFactory(token, baseUrl) }
    val viewModel: PostsViewModel = viewModel(factory = factory)
    val posts by viewModel.posts.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("COEP NEWS", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = { /* search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            MetricsRow(
                publishedToday,
                totalPosts,
                drafts,
                pending,
                newComments,
                spam
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onAnalyticsClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text("ANALYTICS", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onManageClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text("MANAGE", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(posts) { post ->
                    WordPressPostItem(post, viewModel)
                }
            }
        }

        // floating button to create new post
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = Color.Blue,
            contentColor = Color.White,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 32.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }


}

@Composable
fun MetricsRow(
    publishedToday: Int,
    totalPosts: Int,
    drafts: Int,
    pending: Int,
    newComments: Int,
    spam: Int
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MetricItem("Published today", publishedToday.toString())
            MetricItem("Total Posts", totalPosts.toString())
            MetricItem("Drafts", drafts.toString())
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MetricItem("Pending", pending.toString())
            MetricItem("New Comments", newComments.toString())
            MetricItem("Spam", spam.toString())
        }
    }
}

@Composable
fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun WordPressPostItem(post: WordPressPost, viewModel: PostsViewModel) {
    var authorName by remember { mutableStateOf("Loading...") }
    var imageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(post.author) {
        authorName = viewModel.getAuthorName(post.author)
    }

    LaunchedEffect(post.featured_media) {
        imageUrl = viewModel.getFeaturedImageUrl(post.featured_media)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = Html.fromHtml(post.title.rendered, Html.FROM_HTML_MODE_LEGACY).toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "By $authorName on ${post.date.take(10)}",
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = Html.fromHtml(post.excerpt.rendered, Html.FROM_HTML_MODE_LEGACY).toString(),
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 3
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}


