package ru.pasha.feature.home.internal.presentation

import ru.pasha.feature.home.internal.view.CategoriesWidgetView
import ru.pasha.feature.home.internal.view.HomeBottomSheetView

internal data class HomeViewState(
    val categoryState: CategoriesWidgetView.State,
    val markerButtonVisible: Boolean,
    val markers: HomeBottomSheetView.State,
    val addMarkerEnabled: Boolean,
)
