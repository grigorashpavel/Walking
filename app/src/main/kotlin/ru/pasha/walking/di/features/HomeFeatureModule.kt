package ru.pasha.walking.di.features

import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.pasha.feature.history.api.HistoryFeature
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.home.api.HomeFeatureFactory
import ru.pasha.feature.home.api.HomeNavigationProvider
import ru.pasha.walking.di.ApplicationComponent
import ru.pasha.walking.di.CommonFeature

@Module
interface HomeFeatureModule {
    companion object {
        @Provides
        @CommonFeature
        fun provideHomeFeature(withDependenciesComponent: ApplicationComponent): HomeFeature {
            return HomeFeatureFactory.create(withDependenciesComponent)
        }

        @Provides
        fun provideNavigation(historyFeature: HistoryFeature, router: Router): HomeNavigationProvider {
            return object : HomeNavigationProvider {
                override fun navigateToHistory() {
                    router.navigateTo(historyFeature.getHistoryScreen())
                }
            }
        }
    }
}
