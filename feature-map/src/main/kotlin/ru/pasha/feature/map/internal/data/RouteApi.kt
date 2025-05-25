package ru.pasha.feature.map.internal.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.pasha.network.api.ResponseWithStatus
import java.util.UUID

interface RouteApi {
    @POST("v1/route/get-point")
    suspend fun getPoint(
        @Header("X-Idempotency-Key") idempotencyKey: UUID,
        @Body request: GetPointV1Request
    ): Response<ResponseWithStatus<GetPointSuccessData>>

    @POST("v1/route/build-route")
    suspend fun buildRoute(
        @Header("X-Idempotency-Key") idempotencyKey: UUID,
        @Body request: BuildRouteV1Request
    ): Response<ResponseWithStatus<BuildRouteSuccessData>>
}
