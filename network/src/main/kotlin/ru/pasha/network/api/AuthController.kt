package ru.pasha.network.api

import kotlinx.coroutines.flow.Flow

interface AuthController {
    suspend fun refreshSession(): Boolean
    fun getSessionKey(): String?
    val sessionFlow: Flow<String?>
    fun logout()
}
