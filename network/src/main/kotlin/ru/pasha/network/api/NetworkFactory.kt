package ru.pasha.network.api

import ru.pasha.network.internal.createApiFactory

object NetworkFactory {
    fun create(
        deviceIdProvider: () -> String,
        versionProvider: () -> String,
        localeProvider: () -> String,
        authController: AuthController
    ): ApiFactory = createApiFactory(
        deviceIdProvider,
        versionProvider,
        localeProvider,
        authController
    )

    fun createSessionApi(
        deviceIdProvider: () -> String,
        versionProvider: () -> String,
        localeProvider: () -> String,
    ): SessionApi = ru.pasha.network.internal.createSessionApi(
        deviceIdProvider = deviceIdProvider,
        versionProvider = versionProvider,
        localeProvider = localeProvider
    )
}
