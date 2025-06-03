package ru.pasha.feature.home.internal.presentation

import ru.pasha.common.map.Marker
import ru.pasha.common.map.Route

internal data class HomeState(
    val category: Category,
    val interactionModeEnabled: Boolean,
    val previewModeEnabled: Boolean,
    val walkingModeEnabled: Boolean,
    val route: Route?,
    val markers: List<Marker>,
    val isLoading: Boolean,
    val locationTrackingEnabled: Boolean,
)

internal enum class Category {
    Nature, Residential, Roads, None
}
