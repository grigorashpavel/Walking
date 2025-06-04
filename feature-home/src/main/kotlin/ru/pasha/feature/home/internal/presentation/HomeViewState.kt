package ru.pasha.feature.home.internal.presentation

import ru.pasha.feature.home.internal.view.CategoriesWidgetView
import ru.pasha.feature.home.internal.view.HomeBottomSheetView

internal data class HomeViewState(
    val categoryState: CategoriesWidgetView.State,
    val markerButtonVisible: Boolean,
    val sheetContentState: HomeBottomSheetView.State,
    val addMarkerEnabled: Boolean,
    val walkingModeEnabled: Boolean,
    val hasRoute: Boolean,
    val isPreviewMode: Boolean,
    val isLocationButtonVisible: Boolean,
    val walkingStarted: Boolean,
)
