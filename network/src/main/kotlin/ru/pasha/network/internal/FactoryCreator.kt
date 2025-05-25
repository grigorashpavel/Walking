package ru.pasha.network.internal

import ru.pasha.network.api.ApiFactory
import ru.pasha.network.api.AuthController
import ru.pasha.network.api.SessionApi
import ru.pasha.network.internal.client.NetworkClient
import ru.pasha.network.internal.client.createNetworkConfig
import ru.pasha.network.internal.interceptors.AuthorizationInterceptor
import ru.pasha.network.internal.interceptors.createHeadersInterceptor

internal fun createApiFactory(
    deviceIdProvider: () -> String,
    versionProvider: () -> String,
    localeProvider: () -> String,
    authController: AuthController,
): ApiFactory {
    val headersInterceptor = createHeadersInterceptor(
        setOf(
            Header(type = HeaderType.DEVICE_ID, deviceIdProvider),
            Header(type = HeaderType.CLIENT_VERSION, versionProvider),
            Header(type = HeaderType.REQUEST_LANGUAGE, localeProvider),
        )
    )
    val authorizationInterceptor = AuthorizationInterceptor(
        authController = authController,
        authHeader = HeaderType.SESSION_ID
    )

    val client = NetworkClient(
        config = createNetworkConfig(),
        interceptors = setOf(headersInterceptor, authorizationInterceptor)
    )

    return client.apiFactory
}

internal fun createSessionApi(
    deviceIdProvider: () -> String,
    versionProvider: () -> String,
    localeProvider: () -> String,
): SessionApi {
    val headersInterceptor = createHeadersInterceptor(
        setOf(
            Header(type = HeaderType.DEVICE_ID, deviceIdProvider),
            Header(type = HeaderType.CLIENT_VERSION, versionProvider),
            Header(type = HeaderType.REQUEST_LANGUAGE, localeProvider),
        )
    )
    return NetworkClient(
        config = createNetworkConfig(),
        interceptors = setOf(headersInterceptor)
    ).apiFactory.create<SessionApi>()
}
