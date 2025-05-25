package ru.pasha.network.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionApi {
    @POST("v1/end-session")
    suspend fun endSession(
        @Body request: EndSessionV1Request?
    ): Response<ResponseWithStatus<EndSessionSuccessData>>

    @POST("v1/start-session")
    suspend fun startSession(
        @Body request: StartSessionV1Request
    ): Response<ResponseWithStatus<StartSessionSuccessData>>
}

@Serializable
data class StartSessionSuccessData(
    @SerialName("id") val id: String,
    @SerialName("started_at") val startedAt: Long,
    @SerialName("expires_at") val expiresAt: Long? = null,
    @SerialName("message") val message: String? = null
)

@Serializable
data class EndSessionV1Request(
    @SerialName("session_id") val sessionId: String
)

@Serializable
data class StartSessionV1Request(
    @SerialName("token") val token: String
)

@Serializable
data class EndSessionSuccessData(
    @SerialName("is_closed") val isClosed: Boolean,
    @SerialName("message") val message: String? = null
)
