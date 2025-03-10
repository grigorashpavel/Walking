package ru.pasha.network.api

interface AuthManager {
    suspend fun refreshToken(): Boolean
    fun getToken(): String
    fun logout()
}
