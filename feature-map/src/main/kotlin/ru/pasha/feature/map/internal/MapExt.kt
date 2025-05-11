package ru.pasha.feature.map.internal

import org.osmdroid.api.IGeoPoint
import ru.pasha.common.map.GeoPoint

fun IGeoPoint.entity(): GeoPoint = GeoPoint(lat = latitude.toFloat(), lon = longitude.toFloat())

fun IGeoPoint.point(): org.osmdroid.util.GeoPoint = org.osmdroid.util.GeoPoint(latitude, longitude)

fun GeoPoint.point(): org.osmdroid.util.GeoPoint = org.osmdroid.util.GeoPoint(lat.toDouble(), lon.toDouble())
