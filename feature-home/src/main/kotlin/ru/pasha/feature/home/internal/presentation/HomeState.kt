package ru.pasha.feature.home.internal.presentation

import ru.pasha.common.map.Marker

internal data class HomeState(
    val category: Category,
    val interactionModeEnabled: Boolean,
    val markers: List<Marker>,
    val isLoading: Boolean,
)

internal enum class Category {
    Nature, Residential, Roads, None
}
