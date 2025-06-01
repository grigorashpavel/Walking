package ru.pasha.feature.history.api

import android.content.Context
import com.github.terrakok.cicerone.Router
import ru.pasha.common.di.WalkingMapProvider
import ru.pasha.common.map.Route
import ru.pasha.network.api.ApiFactory

interface HistoryDependencies {
    val mapProvider: WalkingMapProvider
    val router: Router
    val apiFactory: ApiFactory
    val context: Context
    val navigationProvider: NavigationProvider
}

interface NavigationProvider {
    fun navigateToPreview(route: Route)
    fun navigateBack()
}
