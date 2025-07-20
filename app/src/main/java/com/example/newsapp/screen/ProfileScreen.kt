package com.example.newsapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.R

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit = {},
    onReAuth: () -> Unit = {},
    onNotificationSettings: () -> Unit = {},
    onNewsletterSettings: () -> Unit = {},
    onAppPreferences: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {

        // Top Row: Profile Picture + Name, Email, Edit Button
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 78.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user), // Replace with actual image
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(20.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("Shreya Shinde", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("shreya@example.com", fontSize = 14.sp, color = Color.Gray)

                Button(onClick = { /* TODO: Edit profile */ },
//                    border = BorderStroke(2.dp, Color.White),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF383838)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Edit Profile")
                }
            }



        }

        Spacer(modifier = Modifier.height(36.dp))



        // Site URL
        Text(
            text = "Site URL:www.shreyashinde.com",
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Settings Buttons
        ProfileSettingItem(title = "Notification Settings")
        ProfileSettingItem(title = "Newsletter Settings")
        ProfileSettingItem(title = "App Preferences")

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        Button(
            onClick = { /* TODO: Logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

@Composable
fun ProfileSettingItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate to settings */ }
            .padding(vertical = 12.dp, horizontal = 28.dp)
            .background(Color(0xFF1F1F1F))
            .height(42.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
            color = Color.White
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Arrow",
            tint = Color.White
        )
    }
}
