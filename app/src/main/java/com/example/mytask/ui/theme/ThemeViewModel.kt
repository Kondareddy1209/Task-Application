package com.example.mytask.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ThemeState(
    val theme: Theme = Theme.SYSTEM
)

class ThemeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ThemeState())
    val uiState: StateFlow<ThemeState> = _uiState.asStateFlow()

    fun onThemeChanged(theme: Theme) {
        _uiState.update { it.copy(theme = theme) }
        // TODO: Persist this setting
    }
}
