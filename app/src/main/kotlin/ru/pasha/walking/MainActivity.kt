package ru.pasha.walking

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.withContext
import ru.pasha.common.di.findDependency
import ru.pasha.common.extensions.isNightMode
import ru.pasha.core.navigation.BaseNavigationActivity
import ru.pasha.core.navigation.NavigationHolder
import ru.pasha.feature.banner.api.BannerUiDependencies
import ru.pasha.network.api.AuthManager
import ru.pasha.walking.auth.SessionStorage
import ru.pasha.walking.di.DaggerActivityComponent
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class MainActivity : BaseNavigationActivity(), AuthManager, BannerUiDependencies {

    @Inject
    lateinit var authSdk: YandexAuthSdk

    @Inject
    lateinit var sessionStorage: SessionStorage

    private val startScreen: Screen by lazy {
        findDependency<NavigationHolder>().startScreen
    }

    private val fragmentFactory: FragmentFactory by lazy {
        findDependency<NavigationHolder>().factory
    }

    @Inject
    override lateinit var cicerone: Cicerone<Router>

    @Inject
    override lateinit var router: Router

    override val navigator: AppNavigator by lazy {
        AppNavigator(
            this,
            R.id.container,
            fragmentFactory = supportFragmentManager.fragmentFactory
        )
    }

    override fun getSessionKey(): String? = sessionStorage.getSessionKey()

    override fun logout() = router.exit()

    private var authLauncher: ActivityResultLauncher<YandexAuthLoginOptions>? = null

    private var authDeferred: CompletableDeferred<String?> = CompletableDeferred()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerActivityComponent.factory()
            .create(this)
            .inject(this)

        authLauncher = registerForActivityResult(authSdk.contract, ::handleResult)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setupWalkingAppTheme()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootChain(startScreen)
        }
    }

    private fun handleResult(result: YandexAuthResult) {
        val authResult = when (result) {
            is YandexAuthResult.Success -> result.token.value
            is YandexAuthResult.Failure -> null
            YandexAuthResult.Cancelled -> null
        }
        authDeferred.complete(authResult)
    }

    override suspend fun refreshSession(): Boolean {
        authDeferred.cancel()
        authDeferred = CompletableDeferred(coroutineContext.job)

        withContext(Dispatchers.Main) {
            authLauncher?.launch(YandexAuthLoginOptions())
        }

        return authDeferred.await()?.let { key ->
            sessionStorage.saveSessionKey(key)
            authDeferred = CompletableDeferred()

            true
        } ?: false
    }

    private fun setupWalkingAppTheme() {
        val theme = if (isNightMode()) {
            ru.pasha.common.R.style.Base_Theme_Walking_Night
        } else {
            ru.pasha.common.R.style.Base_Theme_Walking_Light
        }
        setTheme(theme)
    }

    override val navigateToAuthAction: suspend () -> Unit get() = { refreshSession() }
}
