@file:Suppress("FunctionMaxLength")

package ru.pasha.walking.di.features

import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.pasha.feature.settings.api.SettingsFeature
import ru.pasha.feature.settings.api.SettingsFeatureFactory
import ru.pasha.feature.settings.api.SettingsNavigationProvider
import ru.pasha.network.api.AuthController
import ru.pasha.walking.di.ApplicationComponent

@Module
interface SettingsFeatureModule {
    companion object {
        @Provides
        fun provideSettingsFeature(withDependenciesComponent: ApplicationComponent): SettingsFeature {
            return SettingsFeatureFactory.create(withDependenciesComponent)
        }

        @Provides
        fun provideNavigation(router: Router, authController: AuthController): SettingsNavigationProvider {
            return object : SettingsNavigationProvider {
                override fun navigateBack() {
                    router.exit()
                }

                override fun logout() {
                    authController.logout()
                }
            }
        }
    }
}
