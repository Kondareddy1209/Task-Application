package com.example.mytask.navigation

sealed class Screen(val route: String) {
    object Profile : Screen("profile")
    object ChangePassword : Screen("change_password")
}
