package ru.pasha.network.internal

import ru.pasha.network.api.ApiFactory
import ru.pasha.network.api.AuthManager
import ru.pasha.network.internal.client.NetworkClient
import ru.pasha.network.internal.client.createNetworkConfig
import ru.pasha.network.internal.interceptors.AuthorizationInterceptor
import ru.pasha.network.internal.interceptors.createHeadersInterceptor

internal fun createApiFactory(
    deviceIdProvider: () -> String,
    versionProvider: () -> String,
    localeProvider: () -> String,
    authManager: AuthManager,
): ApiFactory {
    val headersInterceptor = createHeadersInterceptor(
        setOf(
            Header(type = HeaderType.DEVICE_ID, deviceIdProvider),
            Header(type = HeaderType.CLIENT_VERSION, versionProvider),
            Header(type = HeaderType.REQUEST_LANGUAGE, localeProvider),
        )
    )
    val authorizationInterceptor = AuthorizationInterceptor(
        authManager = authManager,
        authHeader = HeaderType.SESSION_ID
    )

    val client = NetworkClient(
        config = createNetworkConfig(),
        interceptors = setOf(headersInterceptor, authorizationInterceptor)
    )

    return client.apiFactory
}
