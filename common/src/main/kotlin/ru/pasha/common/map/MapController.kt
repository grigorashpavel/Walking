package ru.pasha.common.map

import kotlinx.coroutines.flow.Flow

@Suppress("ComplexInterface")
interface MapController {
    val currentLocation: GeoPoint
    fun setCurrentLocation(point: GeoPoint)

    val zoom: Double
    fun setZoom(zoom: Double)

    val isMarkerVisible: Boolean
    fun setCenterMarkerVisibility(show: Boolean)

    fun toggleCreateMarkerFeature(isEnabled: Boolean)

    fun restoreMap()

    val markers: Flow<List<Marker>>
    fun createMarker(): Boolean
}
