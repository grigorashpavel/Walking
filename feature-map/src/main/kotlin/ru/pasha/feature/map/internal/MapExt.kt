package ru.pasha.feature.map.internal

import org.osmdroid.api.IGeoPoint
import ru.pasha.common.map.GeoPoint
import ru.pasha.feature.map.internal.data.Point

internal fun IGeoPoint.entity(): GeoPoint = GeoPoint(lat = latitude.toFloat(), lon = longitude.toFloat())

internal fun IGeoPoint.point(): org.osmdroid.util.GeoPoint = org.osmdroid.util.GeoPoint(latitude, longitude)

internal fun GeoPoint.point(): org.osmdroid.util.GeoPoint = org.osmdroid.util.GeoPoint(lat.toDouble(), lon.toDouble())

internal fun GeoPoint.dto(): Point = Point(lat = lat, lon = lon)
