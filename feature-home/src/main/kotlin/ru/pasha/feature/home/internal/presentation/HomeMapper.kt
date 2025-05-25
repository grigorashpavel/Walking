package ru.pasha.feature.home.internal.presentation

import ru.pasha.common.pattern.BaseMapper
import ru.pasha.feature.home.internal.view.CategoriesWidgetView
import ru.pasha.feature.home.internal.view.HomeBottomSheetView

internal class HomeMapper(
    private val limitPoisChecker: () -> Boolean,
) : BaseMapper<HomeState, HomeViewState>() {
    override fun toViewState(state: HomeState): HomeViewState {
        return HomeViewState(
            categoryState = state.category.toState(),
            markerButtonVisible = state.interactionModeEnabled,
            markers = HomeBottomSheetView.State(state.markers, isLoading = state.isLoading),
            addMarkerEnabled = !limitPoisChecker(),
        )
    }

    private fun Category.toState(): CategoriesWidgetView.State = when (this) {
        Category.Nature -> CategoriesWidgetView.State.User(CategoriesWidgetView.Category.NATURE)
        Category.Residential -> CategoriesWidgetView.State.User(CategoriesWidgetView.Category.RESIDENTIAL)
        Category.Roads -> CategoriesWidgetView.State.User(CategoriesWidgetView.Category.ROADS)
        Category.None -> CategoriesWidgetView.State.Default
    }
}
