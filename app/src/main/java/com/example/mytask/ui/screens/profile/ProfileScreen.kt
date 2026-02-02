package com.example.mytask.ui.screens.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mytask.ui.navigation.Screen
import com.example.mytask.ui.composables.Glassmorphism
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DisableScreenshots() {
    val activity = LocalContext.current as? Activity
    DisposableEffect(Unit) {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Prevent screenshots
    DisableScreenshots()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header with fade and scale animation
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500)) + scaleIn(animationSpec = tween(500), initialScale = 0.8f)
            ) {
                Glassmorphism(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "KR", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Konda Reddy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = "kondareddy@example.com", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val cardEnterAnimation = { delay: Int ->
                fadeIn(animationSpec = tween(500, delayMillis = delay)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(500, delayMillis = delay)
                )
            }

            // Account Information
            AnimatedVisibility(visible = true, enter = cardEnterAnimation(100)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Account Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileInfoItem(title = "Phone Number", value = uiState.phoneNumber)
                        ProfileInfoItem(title = "Account Creation", value = uiState.accountCreationDate)
                        ProfileInfoItem(title = "Auth Method", value = uiState.authMethod)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Security
            AnimatedVisibility(visible = true, enter = cardEnterAnimation(200)) {
                 Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = "Security", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Security", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileSecurityItem(text = "Change Password", onClick = { navController.navigate(Screen.ChangePassword.route) })
                        ProfilePreferenceItem(
                            title = "Enable / Disable Biometric Login",
                            icon = Icons.Default.Fingerprint,
                            isToggle = true,
                            isChecked = uiState.biometricLoginEnabled,
                            onCheckedChange = { viewModel.onBiometricLoginToggled(it, context as FragmentActivity) }
                        )
                        ProfileSecurityItem(text = "Active Sessions", onClick = { navController.navigate(Screen.ActiveSessions.route) })
                        ProfileSecurityItem(text = "Logout from all devices", onClick = { /* TODO: Show confirmation */ })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preferences
            AnimatedVisibility(visible = true, enter = cardEnterAnimation(300)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Preferences", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfilePreferenceItem(
                            title = "Offline map cache",
                            icon = Icons.Default.Download,
                            isToggle = true,
                            isChecked = uiState.offlineCacheEnabled,
                            onCheckedChange = viewModel::onOfflineCacheToggled
                        )
                        ProfilePreferenceItem(
                            title = "Task reminders",
                            icon = Icons.Default.Notifications,
                            isToggle = true,
                            isChecked = uiState.taskRemindersEnabled,
                            onCheckedChange = viewModel::onTaskRemindersToggled
                        )
                        ProfilePreferenceItem(
                            title = "Dark / Light mode",
                            icon = Icons.Default.Palette,
                            onClick = { showThemeDialog = true }
                        )
                        ProfilePreferenceItem(
                            title = "Language selection",
                            icon = Icons.AutoMirrored.Filled.List,
                            onClick = { showLanguageDialog = true }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Privacy & Terms
            AnimatedVisibility(visible = true, enter = cardEnterAnimation(400)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { 
                            val url = "https://policies.google.com/privacy"
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(context, Uri.parse(url))
                         },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = "Privacy", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Privacy & Terms", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions
            Button(
                onClick = { viewModel.onShowLogoutDialog(true) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer, contentColor = MaterialTheme.colorScheme.onTertiaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Logout")
            }

            TextButton(
                onClick = { viewModel.onShowDeleteDialogStep1(true) },
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Account", tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Delete Account", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    // --- Dialogs ---
    if (uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowLogoutDialog(false) },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout from your account?") },
            confirmButton = {
                Button(onClick = { viewModel.onLogout() }) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowLogoutDialog(false) }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState.showDeleteDialogStep1) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowDeleteDialogStep1(false) },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Delete Account?", color = MaterialTheme.colorScheme.error) },
            text = { Text("This action is permanent and cannot be undone. All your data will be deleted.") },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), onClick = { viewModel.onShowDeleteDialogStep2(true) }) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowDeleteDialogStep1(false) }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState.showDeleteDialogStep2) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissDeleteDialogStep2() },
            title = { Text("Confirm Deletion") },
            text = { 
                Column {
                    Text("To confirm, please type 'DELETE' below.")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.deleteConfirmationText,
                        onValueChange = viewModel::onDeleteConfirmationTextChanged,
                        label = { Text("DELETE") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.onDeleteAccount() }, enabled = uiState.deleteConfirmationText == "DELETE", colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Delete Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissDeleteDialogStep2() }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            onDismissRequest = { showThemeDialog = false },
            onThemeSelected = {
                // TODO: Handle theme change
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            onDismissRequest = { showLanguageDialog = false },
            onLanguageSelected = {
                val appLocale = LocaleListCompat.forLanguageTags(it)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        )
    }

}

@Composable
fun ProfileInfoItem(title: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ProfileSecurityItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
    }
}

@Composable
fun ProfilePreferenceItem(
    title: String, 
    icon: ImageVector, 
    isToggle: Boolean = false, 
    isChecked: Boolean = false, 
    onCheckedChange: (Boolean) -> Unit = {}, 
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = if(isToggle) {{ onCheckedChange(!isChecked) }} else onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f))
        if (isToggle) {
            Switch(checked = isChecked, onCheckedChange = onCheckedChange)
        } else {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
fun ThemeSelectionDialog(onDismissRequest: () -> Unit, onThemeSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Theme") },
        text = {
            Column {
                // TODO: Get current theme
                val currentTheme = "Light" 
                Row(Modifier.fillMaxWidth().clickable { onThemeSelected("Light") }) {
                    RadioButton(selected = currentTheme == "Light", onClick = { onThemeSelected("Light") })
                    Text("Light")
                }
                Row(Modifier.fillMaxWidth().clickable { onThemeSelected("Dark") }) {
                    RadioButton(selected = currentTheme == "Dark", onClick = { onThemeSelected("Dark") })
                    Text("Dark")
                }
                Row(Modifier.fillMaxWidth().clickable { onThemeSelected("System") }) {
                    RadioButton(selected = currentTheme == "System", onClick = { onThemeSelected("System") })
                    Text("System Default")
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}

@Composable
fun LanguageSelectionDialog(onDismissRequest: () -> Unit, onLanguageSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Language") },
        text = {
            Column {
                // TODO: Get current language
                val currentLanguage = "en"
                Row(Modifier.fillMaxWidth().clickable { onLanguageSelected("en") }) {
                    RadioButton(selected = currentLanguage == "en", onClick = { onLanguageSelected("en") })
                    Text("English")
                }
                Row(Modifier.fillMaxWidth().clickable { onLanguageSelected("es") }) {
                    RadioButton(selected = currentLanguage == "es", onClick = { onLanguageSelected("es") })
                    Text("Spanish")
                }
                Row(Modifier.fillMaxWidth().clickable { onLanguageSelected("fr") }) {
                    RadioButton(selected = currentLanguage == "fr", onClick = { onLanguageSelected("fr") })
                    Text("French")
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismissRequest) { Text("Cancel") } }
    )
}