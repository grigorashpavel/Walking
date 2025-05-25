@file:Suppress("MatchingDeclarationName", "Filename")

package ru.pasha.feature.map.internal.domain

import ru.pasha.common.Text
import ru.pasha.common.map.GeoPoint
import ru.pasha.common.map.Route

data class GetPointEntity(val point: GeoPoint, val message: Text)

data class BuildRouteEntity(val route: Route, val message: Text)
