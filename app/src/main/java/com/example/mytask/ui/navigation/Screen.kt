package com.example.mytask.ui.navigation

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
    object Auth : Screen("auth")
    object SignIn : Screen("sign_in")
    object SignUp : Screen("sign_up")
    object TaskMap : Screen("task_map")
}
