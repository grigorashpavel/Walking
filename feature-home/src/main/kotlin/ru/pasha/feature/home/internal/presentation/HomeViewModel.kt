package ru.pasha.feature.home.internal.presentation

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.feature.home.api.WalkingMapProvider
import ru.pasha.feature.home.internal.view.CategoriesWidgetView

internal class HomeViewModel @AssistedInject constructor(
    walkingMapProvider: WalkingMapProvider,
) : BaseViewModel<HomeState, HomeViewState>(
    mapper = HomeMapper(),
    initialState = HomeState(category = Category.Nature, interactionModeEnabled = false),
) {

    fun categoriesStateSelected(state: CategoriesWidgetView.State) {
        val category = when (state) {
            CategoriesWidgetView.State.Default -> Category.None
            is CategoriesWidgetView.State.User -> state.category?.toSimpleCategory()
        } ?: previousState?.category ?: Category.Nature

        if (category == this.state.category) return
        updateState { copy(category = category) }
    }

    fun toggleInteractionMode(target: Boolean) {
        updateState { copy(interactionModeEnabled = target) }
    }

    private fun CategoriesWidgetView.Category.toSimpleCategory(): Category {
        return when (this) {
            CategoriesWidgetView.Category.NATURE -> Category.Nature
            CategoriesWidgetView.Category.RESIDENTIAL -> Category.Residential
            CategoriesWidgetView.Category.ROADS -> Category.Roads
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): HomeViewModel
    }
}
