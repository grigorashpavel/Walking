package ru.pasha.feature.map.internal.presentation

import org.osmdroid.util.GeoPoint
import ru.pasha.common.map.Marker

internal data class MapViewState(
    val isCenteredMarkerVisible: Boolean,
    val zoom: Double,
    val location: GeoPoint,
    val markersToDraw: List<Marker>,
    val markersToRemove: List<Marker>,
    val route: List<GeoPoint>?,
)
