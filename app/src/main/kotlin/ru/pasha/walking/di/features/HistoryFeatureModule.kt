package ru.pasha.walking.di.features

import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.pasha.common.map.Route
import ru.pasha.feature.history.api.HistoryFeature
import ru.pasha.feature.history.api.HistoryFeatureFactory
import ru.pasha.feature.history.api.NavigationProvider
import ru.pasha.feature.home.api.HomeArguments
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.walking.di.ApplicationComponent

@Module
interface HistoryFeatureModule {
    companion object {
        @Provides
        fun provideHistoryFeature(withDependenciesComponent: ApplicationComponent): HistoryFeature {
            return HistoryFeatureFactory.create(withDependenciesComponent)
        }

        @Provides
        fun provideNavigation(router: Router, homeFeature: HomeFeature): NavigationProvider {
            return object : NavigationProvider {
                override fun navigateToPreview(route: Route) {
                    router.newRootChain(homeFeature.getHomeScreen(HomeArguments(route)))
                }

                override fun navigateBack() {
                    router.exit()
                }
            }
        }
    }
}
