package ru.pasha.common.map

import kotlinx.coroutines.flow.Flow

interface MapDataSource {
    val coordinates: Flow<GeoPoint>
}
