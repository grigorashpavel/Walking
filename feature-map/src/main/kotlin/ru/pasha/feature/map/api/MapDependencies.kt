package ru.pasha.feature.map.api

import ru.pasha.common.map.GeoPoint

interface MapDependencies {
    val mapSettingsProvider: MapSettingsProvider
}

interface MapSettingsProvider {
    val startLocation: GeoPoint
    val startZoom: Double
    val isCenterMarkerVisible: Boolean
    val maxPois: Int
    val markersColor: List<Int>
}
