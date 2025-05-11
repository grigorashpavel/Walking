package ru.pasha.feature.home.internal.presentation

internal data class HomeState(val category: Category)

internal enum class Category {
    Nature, Residential, Roads, None
}
