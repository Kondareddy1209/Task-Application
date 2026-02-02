package com.example.mytask.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytask.data.AuthRepository
import com.example.mytask.data.AuthRepositoryImpl
import com.example.mytask.util.BiometricAuthManager

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val authRepository: AuthRepository = AuthRepositoryImpl(context)
            val biometricAuthManager = BiometricAuthManager(context)
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(authRepository, biometricAuthManager, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}