package ru.pasha.feature.map.internal.data

import ru.pasha.common.Text
import ru.pasha.common.map.GeoPoint
import ru.pasha.common.orEmpty
import ru.pasha.feature.map.internal.domain.GetPointEntity
import java.util.UUID

fun Point.entity() = GeoPoint(lat = lat, lon = lon)

fun GetPointSuccessData.toEntity(): GetPointEntity = GetPointEntity(
    point = point.entity(),
    message = message?.let { Text.Constant(it) }.orEmpty()
)

fun Route.entity() = ru.pasha.common.map.Route(
    id = UUID.fromString(id),
    name = name,
    path = path.map(Point::mapRoutePoint),
    length = length
)

fun Point.mapRoutePoint() = GeoPoint(lat = lon, lon = lat)
