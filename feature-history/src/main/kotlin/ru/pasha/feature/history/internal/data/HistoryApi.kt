package ru.pasha.feature.history.internal.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import ru.pasha.network.api.ResponseWithStatus
import java.util.UUID

interface HistoryApi {
    @GET("v1/history/get-routes")
    suspend fun getRoutes(
        @Header("X-Idempotency-Key") idempotencyKey: UUID,
        @Query("page") page: Int,
        @Query("size") pageSize: Int,
    ): Response<ResponseWithStatus<GetRoutesSuccessData>>

    @POST("v1/history/get-route")
    suspend fun getRoute(
        @Header("X-Idempotency-Key") idempotencyKey: UUID,
        @Body request: GetRouteV1Request
    ): Response<ResponseWithStatus<GetRouteSuccessData>>
}
