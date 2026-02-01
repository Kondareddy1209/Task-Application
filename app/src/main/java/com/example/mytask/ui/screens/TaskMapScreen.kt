package com.example.mytask.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap

@Composable
fun TaskMapScreen() {
    GoogleMap(modifier = Modifier.fillMaxSize())
}
