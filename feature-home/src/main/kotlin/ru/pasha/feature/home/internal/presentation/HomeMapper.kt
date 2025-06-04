package ru.pasha.feature.home.internal.presentation

import ru.pasha.common.pattern.BaseMapper
import ru.pasha.feature.home.internal.view.CategoriesWidgetView
import ru.pasha.feature.home.internal.view.HomeBottomSheetView

internal class HomeMapper(
    private val reachedMaxMarkers: () -> Boolean,
) : BaseMapper<HomeState, HomeViewState>() {
    override fun toViewState(state: HomeState): HomeViewState {
        return HomeViewState(
            categoryState = state.category.toState(),
            markerButtonVisible = state.interactionModeEnabled,
            sheetContentState = HomeBottomSheetView.State(
                markers = if (state.walkingModeEnabled) listOf() else state.markers,
                isLoading = state.isLoading,
                isMenuVisible = !state.interactionModeEnabled,
                inRouteTime = state.walkingTime?.takeIf { state.walkingModeEnabled },
                route = state.route,
                steps = state.steps?.takeIf { state.walkingModeEnabled && state.stepsTrackingEnabled }
            ),
            addMarkerEnabled = !reachedMaxMarkers(),
            walkingModeEnabled = state.walkingModeEnabled,
            hasRoute = state.route != null,
            isPreviewMode = state.previewModeEnabled,
            isLocationButtonVisible = state.locationTrackingEnabled,
        )
    }

    private fun Category.toState(): CategoriesWidgetView.State = when (this) {
        Category.Nature -> CategoriesWidgetView.State.User(CategoriesWidgetView.Category.NATURE)
        Category.Residential -> CategoriesWidgetView.State.User(CategoriesWidgetView.Category.RESIDENTIAL)
        Category.Roads -> CategoriesWidgetView.State.User(CategoriesWidgetView.Category.ROADS)
        Category.None -> CategoriesWidgetView.State.Default
    }
}
