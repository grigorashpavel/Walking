@file:Suppress("FunctionMaxLength")

package ru.pasha.walking.di.features

import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.pasha.feature.banner.api.BannerFeature
import ru.pasha.feature.banner.api.BannerFeatureFactory
import ru.pasha.feature.banner.api.BannerNavigationProvider
import ru.pasha.feature.banner.api.ConnectionProvider
import ru.pasha.feature.home.api.HomeArguments
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.network.api.ConnectionTracker
import ru.pasha.walking.di.ApplicationComponent

@Module
interface BannerFeatureModule {
    companion object {
        @Provides
        fun provideBannerFeature(withDependenciesComponent: ApplicationComponent): BannerFeature {
            return BannerFeatureFactory.create(withDependenciesComponent)
        }

        @Provides
        fun provideConnectionProvider(
            connectionTracker: ConnectionTracker,
        ): ConnectionProvider = object : ConnectionProvider {
            override val hasConnection: Boolean get() = connectionTracker.hasConnection
        }

        @Provides
        fun provideBannerNavigationProvider(
            connectionProvider: ConnectionProvider,
            homeFeature: HomeFeature,
            router: Router,
        ): BannerNavigationProvider =
            object : BannerNavigationProvider {
                override val navigateToHomeAction: () -> Unit = {
                    val homeScreen = homeFeature.getHomeScreen(
                        HomeArguments(route = null)
                    )
                    router.newRootChain(homeScreen)
                }
            }
    }
}
