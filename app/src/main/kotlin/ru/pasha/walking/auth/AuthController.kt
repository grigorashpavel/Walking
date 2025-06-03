@file:Suppress("MatchingDeclarationName", "Filename")

package ru.pasha.walking.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.pasha.common.di.ApplicationScope
import ru.pasha.network.api.AuthController
import ru.pasha.network.api.SessionApi
import ru.pasha.network.api.StartSessionV1Request
import ru.pasha.network.api.handleApiResponse
import javax.inject.Inject

@ApplicationScope
class AuthControllerImpl @Inject constructor(
    private val authManager: AuthManager,
    private val sessionStorage: SessionStorage,
    private val exitNavigationProvider: ExitNavigationProvider,
    private val sessionApi: SessionApi,
) : AuthController {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun refreshSession(): Boolean {
        withContext(Dispatchers.Main) { authManager.launchAuthScreen() }
        return try {
            val token = when (val authResult = authManager.authResult.first()) {
                is AuthResult.Success -> authResult.token
                else -> return false
            }

            fetchSession(token)?.let { sessionToken ->
                sessionStorage.saveSessionKey(sessionToken)
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun fetchSession(token: String): String? {
        val request = StartSessionV1Request(token)
        handleApiResponse {
            sessionApi.startSession(request)
        }.fold(
            onSuccess = {
                return it.id
            },
            onFailure = {
                it.printStackTrace()
                return null
            }
        )
    }

    override fun getSessionKey(): String? = sessionStorage.getSessionKey()

    override fun logout() {
        sessionStorage.clearSession()
        exitNavigationProvider.exit()
    }
}

interface ExitNavigationProvider {
    fun exit()
}
