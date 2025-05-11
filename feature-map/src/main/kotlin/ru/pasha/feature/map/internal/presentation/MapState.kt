package ru.pasha.feature.map.internal.presentation

import ru.pasha.common.map.GeoPoint
import ru.pasha.common.map.Marker

data class MapState(
    val zoom: Double,
    val center: GeoPoint,
    val centerMarkerVisibility: Boolean,
    val createMarkerOptionEnabled: Boolean,
    val createdMarkers: List<Marker>,

    val stateUpdatingFlag: Boolean,
)
