package com.example.mytask.ui.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ActiveSession(val id: String, val device: String, val location: String, val lastActive: String)

data class ActiveSessionsState(
    val sessions: List<ActiveSession> = emptyList()
)

class ActiveSessionsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveSessionsState())
    val uiState: StateFlow<ActiveSessionsState> = _uiState.asStateFlow()

    init {
        // TODO: Replace with real data from a repository
        _uiState.value = ActiveSessionsState(
            sessions = listOf(
                ActiveSession("1", "Pixel 6 Pro", "New York, NY", "Now"),
                ActiveSession("2", "Galaxy S22 Ultra", "London, UK", "2 hours ago"),
                ActiveSession("3", "iPhone 14 Pro", "San Francisco, CA", "1 day ago")
            )
        )
    }

    fun revokeSession(sessionId: String) {
        _uiState.update {
            val updatedSessions = it.sessions.filter { session -> session.id != sessionId }
            it.copy(sessions = updatedSessions)
        }
    }
}
