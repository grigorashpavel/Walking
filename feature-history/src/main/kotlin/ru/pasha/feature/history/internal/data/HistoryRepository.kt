package ru.pasha.feature.history.internal.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.pasha.common.map.Route
import ru.pasha.feature.history.internal.di.HistoryScope
import ru.pasha.feature.history.internal.domain.PreviewEntity
import ru.pasha.network.api.handleApiResponse
import java.util.UUID
import javax.inject.Inject

@HistoryScope
internal class HistoryRepository @Inject constructor(
    private val historyApi: HistoryApi,
    private val historyLocalManager: HistoryLocalManager,
) {
    suspend fun getRoutes(page: Int, pageSize: Int): Result<GetRoutesSuccessData> =
        withContext(Dispatchers.IO) {
            return@withContext handleApiResponse<GetRoutesSuccessData> {
                historyApi.getRoutes(
                    idempotencyKey = UUID.randomUUID(),
                    page = page,
                    pageSize = pageSize,
                )
            }
        }

    suspend fun getRoute(routeId: UUID): Result<GetRouteSuccessData> =
        withContext(Dispatchers.IO) {
            return@withContext handleApiResponse<GetRouteSuccessData> {
                val request = GetRouteV1Request(id = routeId.toString())
                historyApi.getRoute(
                    idempotencyKey = UUID.randomUUID(),
                    request = request,
                )
            }
        }

    suspend fun getSavedPreviewRoutes(): List<Route>? =
        historyLocalManager.getSavedRoutes()?.map { it.entity() }

    suspend fun getSavedRoute(previewEntity: PreviewEntity): Route? =
        historyLocalManager.loadDataFromFile("${previewEntity.name}.json").getOrNull()?.entity()

    suspend fun saveRoute(route: ru.pasha.feature.history.internal.data.Route) =
        historyLocalManager.saveRouteToFile(data = route)

    suspend fun removeRoute(name: String) = historyLocalManager.removeRouteFile(name)
}
