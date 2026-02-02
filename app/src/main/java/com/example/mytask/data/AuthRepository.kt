package com.example.mytask.data

import kotlinx.coroutines.flow.Flow

data class UserProfile(
    val phoneNumber: String,
    val accountCreationDate: String,
    val authMethod: String,
    val email: String,
    val displayName: String
)

interface AuthRepository {
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun logout()
    suspend fun deleteAccount()
}
