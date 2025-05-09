package ru.pasha.walking

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.withContext
import ru.pasha.common.di.ActivityScope
import ru.pasha.common.extensions.isNightMode
import ru.pasha.core.navigation.BaseNavigationActivity
import ru.pasha.network.api.AuthManager
import ru.pasha.walking.auth.SessionStorage
import ru.pasha.walking.di.DaggerActivityComponent
import ru.pasha.walking.di.StartScreen
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@ActivityScope
class MainActivity : BaseNavigationActivity(), AuthManager {

    @Inject
    lateinit var authSdk: YandexAuthSdk

    @Inject
    lateinit var sessionStorage: SessionStorage

    @Inject
    @StartScreen
    lateinit var startScreen: Screen

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override val navigator: AppNavigator by lazy {
        AppNavigator(
            this,
            R.id.container,
            fragmentFactory = supportFragmentManager.fragmentFactory
        )
    }

    override fun getSessionKey(): String? = sessionStorage.getSessionKey()

    override fun logout() = router.exit()

    private val authLauncher by lazy {
        registerForActivityResult(authSdk.contract, ::handleResult)
    }

    private var authDeferred: CompletableDeferred<String?> = CompletableDeferred()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerActivityComponent.factory()
            .create(this)
            .inject(this)

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
            authLauncher.launch(YandexAuthLoginOptions())
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
}
