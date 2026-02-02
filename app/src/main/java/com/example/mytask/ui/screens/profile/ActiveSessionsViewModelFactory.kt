package com.example.mytask.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActiveSessionsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActiveSessionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActiveSessionsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
