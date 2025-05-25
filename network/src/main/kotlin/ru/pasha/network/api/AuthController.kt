package ru.pasha.network.api

interface AuthController {
    suspend fun refreshSession(): Boolean
    fun getSessionKey(): String?
    fun logout()
}
