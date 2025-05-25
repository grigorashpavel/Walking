package ru.pasha.feature.map.api

import ru.pasha.common.map.GeoPoint
import ru.pasha.network.api.ApiFactory

interface MapDependencies {
    val mapSettingsProvider: MapSettingsProvider
    val apiFactory: ApiFactory
}

interface MapSettingsProvider {
    val startLocation: GeoPoint
    val startZoom: Double
    val isCenterMarkerVisible: Boolean
    val maxPois: Int
    val markersColor: List<Int>
}
