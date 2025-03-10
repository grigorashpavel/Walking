package ru.pasha.network.api

import ru.pasha.network.internal.createApiFactory

object NetworkFactory {
    fun create(
        deviceIdProvider: () -> String,
        versionProvider: () -> String,
        localeProvider: () -> String,
        authManager: AuthManager
    ): ApiFactory = createApiFactory(
        deviceIdProvider,
        versionProvider,
        localeProvider,
        authManager
    )
}
