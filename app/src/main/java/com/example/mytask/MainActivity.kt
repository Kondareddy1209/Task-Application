package com.example.mytask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytask.ui.navigation.AppNavigation
import com.example.mytask.ui.theme.MyTaskTheme
import com.example.mytask.ui.theme.ThemeViewModel
import com.example.mytask.ui.theme.ThemeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModelFactory())
            MyTaskTheme(themeViewModel = themeViewModel) {
                AppNavigation()
            }
        }
    }
}
