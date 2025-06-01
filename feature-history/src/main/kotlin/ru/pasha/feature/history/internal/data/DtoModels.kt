package ru.pasha.feature.history.internal.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRouteV1Request(
    @SerialName("id")
    val id: String,
)

@Serializable
data class GetRouteSuccessData(
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
data class PreviewRoute(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
)

@Serializable
data class Point(
    @SerialName("lat")
    val lat: Float,

    @SerialName("lon")
    val lon: Float
)

@Serializable
data class GetRoutesSuccessData(
    @SerialName("routes")
    val routes: List<PreviewRoute>,

    @SerialName("message")
    val message: String?,
)
