package com.example.mytask.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val context: Context) : AuthRepository {
    override fun getUserProfile(): Flow<UserProfile?> {
        return flow {
            emit(
                UserProfile(
                    phoneNumber = "1234567890",
                    accountCreationDate = "01/01/2023",
                    authMethod = "Email",
                    email = "kondareddy@example.com",
                    displayName = "Konda Reddy"
                )
            )
        }
    }

    override suspend fun logout() {
        // TODO: Implement logout
    }

    override suspend fun deleteAccount() {
        // TODO: Implement delete account
    }
}