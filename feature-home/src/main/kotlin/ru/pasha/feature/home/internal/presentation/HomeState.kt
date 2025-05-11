package ru.pasha.feature.home.internal.presentation

internal data class HomeState(val category: Category, val interactionModeEnabled: Boolean)

internal enum class Category {
    Nature, Residential, Roads, None
}
