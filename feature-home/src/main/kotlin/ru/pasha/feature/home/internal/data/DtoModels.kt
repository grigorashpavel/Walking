@file:Suppress("Filename", "MatchingDeclarationName")

package ru.pasha.feature.home.internal.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackSuccessData(
    @SerialName("message")
    val message: String? = null,
)

@Serializable
data class FeedbackV1Request(
    @SerialName("message")
    val message: String,
    @SerialName("trace_id")
    val traceId: String? = null,
    @SerialName("route_id")
    val routeId: String? = null,
    @SerialName("rating")
    val rating: Int? = null
)
