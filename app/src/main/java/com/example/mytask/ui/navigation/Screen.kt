package com.example.mytask.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Landing : Screen("landing")
    object Auth : Screen("auth")
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object TaskList : Screen("tasklist")
    object AddTask : Screen("addtask")
    object TaskMap : Screen("taskmap")
    object Profile : Screen("profile")
    object ChangePassword : Screen("change_password")
    object ActiveSessions : Screen("active_sessions")
}
