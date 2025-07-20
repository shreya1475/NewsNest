package com.example.newsapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.screen.AnalyticsScreen
import com.example.newsapp.screen.DashboardScreen
import com.example.newsapp.screen.LoginScreen
import com.example.newsapp.screen.PostEditorScreen
import com.example.newsapp.screen.ProfileScreen
import com.example.newsapp.screen.WelcomeScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {

        composable("welcome") {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = { token, baseUrl ->
                    val encodedBaseUrl = URLEncoder.encode(baseUrl, StandardCharsets.UTF_8.toString())
                    navController.navigate("dashboard/$token/$encodedBaseUrl") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }


        composable("dashboard/{token}/{baseUrl}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val baseUrlEncoded = backStackEntry.arguments?.getString("baseUrl") ?: ""
            val baseUrl = URLDecoder.decode(baseUrlEncoded, StandardCharsets.UTF_8.toString())

            DashboardScreen(
                token = token,
                baseUrl = baseUrl,
                navController = navController,
                onAnalyticsClick = { navController.navigate("analytics") },
                onManageClick = { /* ... */ },
                onFabClick = { navController.navigate("editor") }
            )
        }


        composable("analytics") {
            AnalyticsScreen(
                navController = navController,
                onAnalyticsClick = { /* maybe reload data or nothing */ },
                onManageClick = { /* optional */ }
            )
        }
        composable("editor") {
            PostEditorScreen()
        }


        composable("profile") {
            ProfileScreen(
                onEditProfile = { /* TODO: Navigate to Edit Profile screen if implemented */ },
                onReAuth = { /* TODO: Handle re-authentication */ },
                onNotificationSettings = { /* TODO: Navigate to Notification Settings */ },
                onNewsletterSettings = { /* TODO: Navigate to Newsletter Settings */ },
                onAppPreferences = { /* TODO: Navigate to App Preferences */ },
                onLogout = {
                    navController.navigate("welcome") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
    }
}
