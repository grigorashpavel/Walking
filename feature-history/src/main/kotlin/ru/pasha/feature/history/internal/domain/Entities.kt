@file:Suppress("MatchingDeclarationName", "Filename")

package ru.pasha.feature.history.internal.domain

import ru.pasha.common.Text
import ru.pasha.common.map.Route
import ru.pasha.feature.history.internal.data.PreviewRoute
import java.util.UUID

internal data class GetRouteEntity(val route: Route, val message: Text)
internal data class GetRoutesEntity(val routes: List<PreviewRoute>, val message: Text)

internal data class PreviewEntity(val id: UUID, val name: String, val downloaded: Boolean) {
    override fun equals(other: Any?): Boolean =
        (other as? PreviewEntity)?.id?.let { it == id } ?: false

    override fun hashCode(): Int = id.hashCode()
}
