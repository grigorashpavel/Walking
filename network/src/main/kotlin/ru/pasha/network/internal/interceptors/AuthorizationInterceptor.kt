package ru.pasha.network.internal.interceptors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import ru.pasha.network.api.AuthController
import ru.pasha.network.internal.HeaderType
import java.net.HttpURLConnection

internal class AuthorizationInterceptor(
    private val authController: AuthController,
    private val authHeader: HeaderType,
) : Interceptor {

    private val tokenRefreshMutex = Mutex()

    @Volatile
    private var token = authController.getSessionKey()

    init {
        authController.sessionFlow.onEach {
            tokenRefreshMutex.withLock {
                token = it
            }
        }.launchIn(CoroutineScope(SupervisorJob()))
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var token = this.token

        val request = chain.request().newBuilder()
            .header(authHeader.value, token.orEmpty())
            .build()

        val initialResponse = chain.proceed(request)
        if (
            initialResponse.code != HttpURLConnection.HTTP_UNAUTHORIZED &&
            initialResponse.code != HttpURLConnection.HTTP_INTERNAL_ERROR
        ) {
            return initialResponse
        }

        runBlocking {
            tokenRefreshMutex.withLock {
                authController.refreshSession()
                this@AuthorizationInterceptor.token = authController.getSessionKey()
                token = this@AuthorizationInterceptor.token
            }
        }
        val newRequest = chain.request().newBuilder()
            .header(authHeader.value, token.orEmpty())
            .build()

        val response = chain.proceed(newRequest)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            authController.logout()
        }
        return response
    }
}
