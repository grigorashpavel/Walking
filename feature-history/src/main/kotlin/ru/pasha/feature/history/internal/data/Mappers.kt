package ru.pasha.feature.history.internal.data

import ru.pasha.common.map.GeoPoint
import ru.pasha.feature.history.internal.domain.PreviewEntity
import java.util.UUID

fun Route.entity() = ru.pasha.common.map.Route(
    id = UUID.fromString(id),
    name = name,
    path = path.map(Point::mapRoutePoint),
    length = length
)

fun PreviewRoute.entity() = PreviewEntity(
    id = UUID.fromString(id),
    name = name,
    downloaded = false,
)

fun Point.mapRoutePoint() = GeoPoint(lat = lon, lon = lat)
