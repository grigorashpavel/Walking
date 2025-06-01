package ru.pasha.feature.history.internal.data

import ru.pasha.common.map.GeoPoint
import ru.pasha.feature.history.internal.domain.PreviewEntity
import java.util.UUID

internal fun Route.entity() = ru.pasha.common.map.Route(
    id = UUID.fromString(id),
    name = name,
    path = path.map(Point::mapRoutePoint),
    length = length
)

internal fun PreviewRoute.entity() = PreviewEntity(
    id = UUID.fromString(id),
    name = name,
    downloaded = false,
)

internal fun Point.mapRoutePoint() = GeoPoint(lat = lon, lon = lat)
