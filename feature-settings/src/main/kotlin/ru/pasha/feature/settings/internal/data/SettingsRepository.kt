package ru.pasha.feature.settings.internal.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.pasha.feature.settings.internal.di.SettingsScope
import ru.pasha.network.api.handleApiResponse
import java.util.UUID
import javax.inject.Inject

@SettingsScope
internal class SettingsRepository @Inject constructor(
    private val settingsApi: SettingsApi,
) {
    suspend fun endSession(): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext handleApiResponse<EndSessionSuccessData> {
                settingsApi.endSession(UUID.randomUUID())
            }.getOrNull() != null
        }
}
