package ru.pasha.feature.map.api

import kotlinx.coroutines.flow.Flow
import ru.pasha.common.map.GeoPoint

interface MapController {
    val pointsFlow: Flow<GeoPoint>
    fun getCurrentLocation(): GeoPoint
}
