package com.example.newsapp.reader

import android.text.Html
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage


@Composable
fun ReaderScreen(posts: List<Post>, navController: NavController) {
    val viewModel: PostViewModel = viewModel()
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
            BottomNavigationBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TopBar(
                isSearching = isSearching,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchClick = { isSearching = true },
                onClearSearch = {
                    searchQuery = ""
                    isSearching = false
                }
            )

            CategoryTabs()

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                }
                else -> {
                    val filteredPosts = if (searchQuery.isBlank()) posts else posts.filter {
                        Html.fromHtml(it.title.rendered, Html.FROM_HTML_MODE_LEGACY)
                            .toString()
                            .contains(searchQuery, ignoreCase = true)
                    }
                    NewsList(filteredPosts, navController)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isSearching: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onClearSearch: () -> Unit
) {
    if (isSearching) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search news...") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = onClearSearch) {
                    Icon(Icons.Default.Close, contentDescription = "Clear Search")
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "COEP News",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun CategoryTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 8.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val categories = listOf("All", "Sports", "Politics", "Business", "Health", "Travel", "Science")
        categories.forEachIndexed { index, category ->
            Text(
                text = category,
                color = if (index == 0) Color.White else Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .border(
                        BorderStroke(
                            width = if (index == 0) 1.dp else 0.dp,
                            color = if (index == 0) Color.Gray else Color.Transparent
                        )
                    )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "See all",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Composable
fun NewsList(posts: List<Post>, navController: NavController) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        itemsIndexed(posts) { index, post ->
            NewsCardFromPost(post = post, onClick = {
                navController.navigate("detail/$index")
            })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NewsCardFromPost(post: Post, onClick: () -> Unit) {
    val cleanTitle = Html.fromHtml(post.title.rendered, Html.FROM_HTML_MODE_LEGACY).toString()
    val cleanExcerpt = Html.fromHtml(post.excerpt.rendered, Html.FROM_HTML_MODE_LEGACY).toString()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        post.imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 8.dp)
            )
        }

        Text(
            text = cleanTitle,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = cleanExcerpt,
            color = Color.Gray,
            fontSize = 14.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Published on: ${post.date.substringBefore("T")}",
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Filled.Bookmarks, contentDescription = "Bookmark") },
            label = { Text("Bookmark") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}


@Composable
fun PostDetailScreen(post: Post) {
    val title = Html.fromHtml(post.title.rendered, Html.FROM_HTML_MODE_LEGACY).toString()
    val content = Html.fromHtml(post.content.rendered, Html.FROM_HTML_MODE_LEGACY).toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 16.dp
                )
                .height(56.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "COEP News",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }



        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.DarkGray)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = post.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

