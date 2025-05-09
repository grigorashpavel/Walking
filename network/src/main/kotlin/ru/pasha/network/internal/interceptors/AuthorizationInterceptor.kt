package ru.pasha.network.internal.interceptors

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import ru.pasha.network.api.AuthManager
import ru.pasha.network.internal.HeaderType
import java.net.HttpURLConnection

internal class AuthorizationInterceptor(
    private val authManager: AuthManager,
    private val authHeader: HeaderType,
) : Interceptor {

    private val tokenRefreshMutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val initialResponse = chain.proceed(request)

        if (initialResponse.code != HttpURLConnection.HTTP_UNAUTHORIZED) {
            return initialResponse
        }

        val tokenUpdated = runBlocking {
            tokenRefreshMutex.withLock {
                authManager.refreshSession()
            }
        }
        val token = authManager.getSessionKey()

        if (!tokenUpdated || token == null) {
            authManager.logout()
            return initialResponse
        }

        val newRequest = request.newBuilder()
            .header(authHeader.value, token)
            .build()

        return chain.proceed(newRequest)
    }
}
