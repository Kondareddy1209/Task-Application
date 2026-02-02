package com.example.mytask.ui.screens.profile

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytask.data.AuthRepository
import com.example.mytask.util.BiometricAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore("settings")

data class ProfileState(
    val phoneNumber: String = "",
    val accountCreationDate: String = "",
    val authMethod: String = "",
    val offlineCacheEnabled: Boolean = true,
    val taskRemindersEnabled: Boolean = true,
    val biometricLoginEnabled: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showDeleteDialogStep1: Boolean = false,
    val showDeleteDialogStep2: Boolean = false,
    val deleteConfirmationText: String = ""
)

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val biometricAuthManager: BiometricAuthManager,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val offlineCacheKey = booleanPreferencesKey("offline_cache")
    private val taskRemindersKey = booleanPreferencesKey("task_reminders")

    init {
        viewModelScope.launch {
            authRepository.getUserProfile().collect { userProfile ->
                if (userProfile != null) {
                    _uiState.update {
                        it.copy(
                            phoneNumber = userProfile.phoneNumber.let { "****-***-${it.takeLast(4)}" },
                            accountCreationDate = userProfile.accountCreationDate,
                            authMethod = userProfile.authMethod
                        )
                    }
                }
            }
        }
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            val preferences = context.dataStore.data.first()
            _uiState.update {
                it.copy(
                    offlineCacheEnabled = preferences[offlineCacheKey] ?: true,
                    taskRemindersEnabled = preferences[taskRemindersKey] ?: true
                )
            }
        }
    }

    fun onOfflineCacheToggled(enabled: Boolean) {
        _uiState.update { it.copy(offlineCacheEnabled = enabled) }
        viewModelScope.launch {
            context.dataStore.edit { settings ->
                settings[offlineCacheKey] = enabled
            }
        }
    }

    fun onTaskRemindersToggled(enabled: Boolean) {
        _uiState.update { it.copy(taskRemindersEnabled = enabled) }
        viewModelScope.launch {
            context.dataStore.edit { settings ->
                settings[taskRemindersKey] = enabled
            }
        }
    }

    fun onBiometricLoginToggled(enabled: Boolean, activity: FragmentActivity) {
        if (enabled) {
            if (biometricAuthManager.isBiometricAuthAvailable()) {
                biometricAuthManager.showBiometricPrompt(
                    activity = activity,
                    onSuccess = {
                        _uiState.update { it.copy(biometricLoginEnabled = true) }
                    },
                    onError = { _, _ ->
                        // Handle error
                    }
                )
            } else {
                // Handle biometric not available
            }
        } else {
            _uiState.update { it.copy(biometricLoginEnabled = false) }
        }
    }

    fun onShowLogoutDialog(show: Boolean) {
        _uiState.update { it.copy(showLogoutDialog = show) }
    }

    fun onLogout() {
        viewModelScope.launch {
            authRepository.logout()
            onShowLogoutDialog(false)
        }
    }

    fun onShowDeleteDialogStep1(show: Boolean) {
        _uiState.update { it.copy(showDeleteDialogStep1 = show) }
    }
    
    fun onShowDeleteDialogStep2(show: Boolean) {
        _uiState.update { it.copy(showDeleteDialogStep1 = false, showDeleteDialogStep2 = show) }
    }

    fun onDismissDeleteDialogStep2() {
        _uiState.update { it.copy(showDeleteDialogStep2 = false, deleteConfirmationText = "") }
    }

    fun onDeleteConfirmationTextChanged(text: String) {
        _uiState.update { it.copy(deleteConfirmationText = text) }
    }

    fun onDeleteAccount() {
        viewModelScope.launch {
            if (uiState.value.deleteConfirmationText == "DELETE") {
                authRepository.deleteAccount()
                onDismissDeleteDialogStep2()
            }
        }
    }
}