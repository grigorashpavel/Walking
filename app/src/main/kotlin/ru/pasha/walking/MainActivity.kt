package ru.pasha.walking

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.flow.Flow
import ru.pasha.common.di.findDependency
import ru.pasha.core.navigation.BaseNavigationActivity
import ru.pasha.core.navigation.NavigationHolder
import ru.pasha.feature.banner.api.BannerUiDependencies
import ru.pasha.feature.map.api.MapUiDependencies
import ru.pasha.feature.map.api.StepController
import ru.pasha.walking.auth.AuthManager
import ru.pasha.walking.auth.AuthManagerProvider
import ru.pasha.walking.di.DaggerActivityComponent
import javax.inject.Inject

@SuppressLint("HardwareIds")
class MainActivity : BaseNavigationActivity(), BannerUiDependencies, MapUiDependencies {

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

    @Inject
    lateinit var stepController: StepController

    override val stepsFlow: Flow<Int> get() = stepController.stepsFlow

    override val navigator: AppNavigator by lazy {
        AppNavigator(
            this,
            R.id.container,
            fragmentFactory = supportFragmentManager.fragmentFactory
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WalkingApp).settingsManager.applySettings()

        DaggerActivityComponent.factory()
            .create(this)
            .inject(this)

        authManager.registerLauncher(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootChain(startScreen)
        }
    }

    override fun onStart() {
        super.onStart()
        stepController.startCounting()
    }

    override fun onStop() {
        stepController.stopCounting()
        super.onStop()
    }

    override val navigateToAuthAction: suspend () -> Boolean get() = {
        (applicationContext as WalkingApp).authController.refreshSession()
    }
}
