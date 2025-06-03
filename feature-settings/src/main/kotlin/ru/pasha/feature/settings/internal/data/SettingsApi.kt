package ru.pasha.feature.settings.internal.data

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import ru.pasha.network.api.ResponseWithStatus
import java.util.UUID

internal interface SettingsApi {
    @POST("v1/end-session")
    suspend fun endSession(
        @Header("X-Idempotency-Key") idempotencyKey: UUID,
    ): Response<ResponseWithStatus<EndSessionSuccessData>>
}
