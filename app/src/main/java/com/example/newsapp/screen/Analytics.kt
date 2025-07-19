package com.example.newsapp.screen
//test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.newsapp.R



@Composable
fun AnalyticsScreen(
    navController: NavController,
    onAnalyticsClick: () -> Unit,
    onManageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("OECP NEWS", color = Color.White, fontWeight = FontWeight.Bold)
            Row {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard(title = "BEST TIME TO POST:", value = "5pm")
            StatCard(title = "Total Pageviews:", value = "1400")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Traffic Chart
        Text("Traffic", color = Color.White, fontWeight = FontWeight.Bold)
        ImageCard(imageRes = R.drawable.analytics)

        Spacer(modifier = Modifier.height(16.dp))

        // Post Performance Chart
        Text("Post Performance", color = Color.White, fontWeight = FontWeight.Bold)
        ImageCard(imageRes = R.drawable.analytics)

        Spacer(modifier = Modifier.height(16.dp))

        // Table Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Title", color = Color.White)
            Text("Engage. Rate", color = Color.White)
            Text("Bounce Rate", color = Color.White)
        }

        // Table Rows
        repeat(4) {
            TableRowItem(title = "Hello World", engagementRate = "1.17%", bounceRate = "50%")
        }
    }
}


@Composable
fun StatCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(title, color = Color.White, fontSize = 12.sp)
        Text(value, color = Color.Cyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun ImageCard(imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun TableRowItem(title: String, engagementRate: String, bounceRate: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = false, onCheckedChange = {}, colors = CheckboxDefaults.colors(checkedColor = Color.White))
            Text(title, color = Color.White)
        }
        Text(engagementRate, color = Color.White)
        Text(bounceRate, color = Color.White)
    }
}
