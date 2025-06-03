package ru.pasha.feature.home.internal.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.pasha.network.api.ResponseWithStatus
import java.util.UUID

internal interface HomeApi {

    @POST("v1/feedback")
    suspend fun report(
        @Header("X-Idempotency-Key") idempotencyKey: UUID,
        @Body request: FeedbackV1Request,
    ): Response<ResponseWithStatus<FeedbackSuccessData>>
}
