package ru.pasha.feature.map.internal.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.pasha.common.map.GeoPoint
import ru.pasha.feature.map.internal.data.BuildRouteSuccessData
import ru.pasha.feature.map.internal.data.BuildRouteV1Request
import ru.pasha.feature.map.internal.data.GetPointSuccessData
import ru.pasha.feature.map.internal.data.GetPointV1Request
import ru.pasha.feature.map.internal.data.RouteApi
import ru.pasha.feature.map.internal.di.MapScope
import ru.pasha.feature.map.internal.dto
import ru.pasha.network.api.handleApiResponse
import java.util.UUID
import javax.inject.Inject

@MapScope
class RouteRepository @Inject constructor(private val api: RouteApi) {
    suspend fun getNearestPoint(currentPoint: GeoPoint): Result<GetPointSuccessData> =
        withContext(Dispatchers.IO) {
            val request = GetPointV1Request(currentPoint.dto())
            return@withContext handleApiResponse<GetPointSuccessData> {
                api.getPoint(UUID.randomUUID(), request)
            }
        }

    suspend fun buildRoute(pois: List<GeoPoint>, name: String?): Result<BuildRouteSuccessData> =
        withContext(Dispatchers.IO) {
            val request = BuildRouteV1Request(name = name, points = pois.map { it.dto() })
            return@withContext handleApiResponse<BuildRouteSuccessData> {
                api.buildRoute(
                    idempotencyKey = UUID.randomUUID(),
                    request = request
                )
            }
        }
}
