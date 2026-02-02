package com.example.mytask.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mytask.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(navController: NavController) {
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }
    var cardVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        logoVisible = true
        delay(200)
        textVisible = true
        delay(200)
        cardVisible = true
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Branding Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF26C6DA), // Teal
                            Color(0xFF42A5F5)  // Blue
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = logoVisible,
                    enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "MAPTASK Logo",
                        modifier = Modifier.size(100.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = textVisible,
                    enter = slideInVertically(animationSpec = tween(durationMillis = 500), initialOffsetY = { -it }) + fadeIn()
                ) {
                    Text(
                        text = "MAPTASK",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                AnimatedVisibility(
                    visible = textVisible,
                    enter = slideInVertically(animationSpec = tween(durationMillis = 500, delayMillis = 100), initialOffsetY = { -it }) + fadeIn()
                ) {
                    Text(
                        text = "Manage Tasks by Location",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Bottom Authentication Section
        AnimatedVisibility(
            visible = cardVisible,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(durationMillis = 600, delayMillis = 300)) + fadeIn()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.05f,
                        animationSpec = infiniteRepeatable(tween(1000), repeatMode = androidx.compose.animation.core.RepeatMode.Reverse),
                        label = "pulseScale"
                    )
                    Text(
                        text = "Get Started",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .scale(pulseScale)
                    )

                    val signInInteractionSource = remember { MutableInteractionSource() }
                    val isSignInPressed by signInInteractionSource.collectIsPressedAsState()
                    val signInScale = if (isSignInPressed) 0.95f else 1f

                    Button(
                        onClick = { navController.navigate(Screen.SignIn.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(signInScale),
                        interactionSource = signInInteractionSource
                    ) {
                        Text("Sign In", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val signUpInteractionSource = remember { MutableInteractionSource() }
                    val isSignUpPressed by signUpInteractionSource.collectIsPressedAsState()
                    val signUpScale = if (isSignUpPressed) 0.95f else 1f

                    OutlinedButton(
                        onClick = { navController.navigate(Screen.SignUp.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(signUpScale),
                        interactionSource = signUpInteractionSource
                    ) {
                        Text("Sign Up", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
