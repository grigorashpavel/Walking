package ru.pasha.walking

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.pasha.common.di.findDependency
import ru.pasha.common.extensions.isNightMode
import ru.pasha.core.navigation.BaseNavigationActivity
import ru.pasha.core.navigation.NavigationHolder
import ru.pasha.feature.banner.api.BannerUiDependencies
import ru.pasha.walking.auth.AuthManager
import ru.pasha.walking.auth.AuthManagerProvider
import ru.pasha.walking.di.DaggerActivityComponent

@SuppressLint("HardwareIds")
class MainActivity : BaseNavigationActivity(), BannerUiDependencies {

    private val startScreen: Screen by lazy {
        findDependency<NavigationHolder>().startScreen
    }

    private val fragmentFactory: FragmentFactory by lazy {
        findDependency<NavigationHolder>().factory
    }

    private val authManager: AuthManager by lazy {
        findDependency<AuthManagerProvider>().authManager
    }

    override val cicerone: Cicerone<Router> by lazy {
        findDependency<NavigationHolder>().cicerone
    }

    override val router: Router by lazy {
        findDependency<NavigationHolder>().router
    }

    override val navigator: AppNavigator by lazy {
        AppNavigator(
            this,
            R.id.container,
            fragmentFactory = supportFragmentManager.fragmentFactory
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerActivityComponent.factory()
            .create(this)
            .inject(this)

        authManager.registerLauncher(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setupWalkingAppTheme()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootChain(startScreen)
        }
    }

    private fun setupWalkingAppTheme() {
        val theme = if (isNightMode()) {
            ru.pasha.common.R.style.Base_Theme_Walking_Night
        } else {
            ru.pasha.common.R.style.Base_Theme_Walking_Light
        }
        setTheme(theme)
    }

    override val navigateToAuthAction: suspend () -> Unit get() = {
        authManager.launchAuthScreen()
    }
}
