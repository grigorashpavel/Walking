package ru.pasha.feature.home.internal.presentation

import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.pasha.common.pattern.BaseViewModel
import ru.pasha.feature.home.api.WalkingMapProvider
import ru.pasha.feature.home.internal.view.CategoriesWidgetView

internal class HomeViewModel @AssistedInject constructor(
    walkingMapProvider: WalkingMapProvider,
) : BaseViewModel<HomeState, HomeViewState>(
    mapper = HomeMapper(),
    initialState = HomeState(category = Category.Nature)
) {
    init {
        walkingMapProvider.pointsProvider.onEach { point ->
            println("Home VM: $point")
        }.launchIn(viewModelScope)
    }

    fun categoriesStateSelected(state: CategoriesWidgetView.State) {
        val category = when (state) {
            CategoriesWidgetView.State.Default -> Category.None
            is CategoriesWidgetView.State.User -> state.category?.toSimpleCategory()
        } ?: previousState?.category ?: Category.Nature

        if (category == this.state.category) return
        updateState { copy(category = category) }
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
