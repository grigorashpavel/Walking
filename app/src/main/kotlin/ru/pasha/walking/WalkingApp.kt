package ru.pasha.walking

import android.app.Application
import android.content.res.Configuration
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import ru.pasha.common.initImageLoader
import ru.pasha.core.navigation.NavigationHolder
import ru.pasha.feature.settings.api.SettingsManager
import ru.pasha.network.api.AuthController
import ru.pasha.walking.auth.AuthManager
import ru.pasha.walking.auth.AuthManagerProvider
import ru.pasha.walking.di.DaggerApplicationComponent
import ru.pasha.walking.di.StartScreen
import javax.inject.Inject

class WalkingApp : Application(), NavigationHolder, AuthManagerProvider {
    override fun onCreate() {
        DaggerApplicationComponent.factory()
            .create(applicationContext)
            .inject(this)

        super.onCreate()

        settingsManager.applySettings()
        initImageLoader(applicationContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        settingsManager.applySettings()
    }

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    override lateinit var factory: FragmentFactory

    @Inject
    @StartScreen
    override lateinit var startScreen: Screen

    @Inject
    override lateinit var cicerone: Cicerone<Router>

    @Inject
    override lateinit var router: Router

    @Inject
    override lateinit var authManager: AuthManager

    @Inject
    lateinit var authController: AuthController
}
