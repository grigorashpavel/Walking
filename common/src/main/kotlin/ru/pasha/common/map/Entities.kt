package ru.pasha.common.map

import androidx.annotation.ColorInt
import java.util.UUID

data class GeoPoint(val lat: Float, val lon: Float)

data class Marker(val id: Int, val point: GeoPoint, @ColorInt val color: Int)

data class Route(val id: UUID, val name: String, val path: List<GeoPoint>, val length: Double)
