package ru.pasha.feature.map.internal.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildRouteV1Request(
    @SerialName("name")
    val name: String?,
    @SerialName("points")
    val points: List<Point>,
)

@Serializable
data class BuildRouteSuccessData(
    @SerialName("route")
    val route: Route,

    @SerialName("message")
    val message: String? = null
)

@Serializable
data class Route(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("path")
    val path: List<Point>,
    @SerialName("length")
    val length: Double,
)

@Serializable
data class GetPointV1Request(
    @SerialName("point")
    val point: Point
)

@Serializable
data class Point(
    @SerialName("lat")
    val lat: Float,

    @SerialName("lon")
    val lon: Float
)

@Serializable
data class GetPointSuccessData(
    @SerialName("point")
    val point: Point,

    @SerialName("message")
    val message: String? = null
)
