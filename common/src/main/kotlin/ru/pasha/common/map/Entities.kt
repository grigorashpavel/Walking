package ru.pasha.common.map

import androidx.annotation.ColorInt

data class GeoPoint(val lat: Float, val lon: Float)

data class Marker(val id: Int, val point: GeoPoint, @ColorInt val color: Int)
