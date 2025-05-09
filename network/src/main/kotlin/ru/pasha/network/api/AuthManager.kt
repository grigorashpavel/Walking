package ru.pasha.network.api

interface AuthManager {
    suspend fun refreshSession(): Boolean
    fun getSessionKey(): String?
    fun logout()
}
