package com.example.mytask.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytask.ui.screens.AddTaskScreen
import com.example.mytask.ui.screens.LandingScreen
import com.example.mytask.ui.screens.SplashScreen
import com.example.mytask.ui.screens.TaskListScreen
import com.example.mytask.ui.screens.TaskMapScreen
import com.example.mytask.ui.screens.auth.AuthScreen
import com.example.mytask.ui.screens.auth.SignInScreen
import com.example.mytask.ui.screens.auth.SignUpScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Landing.route) {
            LandingScreen(navController = navController)
        }
        composable(Screen.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.TaskList.route) {
            TaskListScreen(navController = navController)
        }
        composable(Screen.AddTask.route) {
            AddTaskScreen(navController = navController)
        }
        composable(Screen.TaskMap.route) {
            TaskMapScreen()
        }
    }
}
