package com.example.mytask.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytask.ui.screens.profile.ChangePasswordScreen
import com.example.mytask.ui.screens.profile.ProfileScreen

@Composable
fun ProfileNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Profile.route) {
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(navController = navController)
        }
    }
}
