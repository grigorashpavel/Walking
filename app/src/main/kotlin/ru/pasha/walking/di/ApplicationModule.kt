package ru.pasha.walking.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.fragment.app.FragmentFactory
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.pasha.common.di.ApplicationScope
import ru.pasha.common.di.WalkingMapProvider
import ru.pasha.common.map.MapController
import ru.pasha.core.navigation.ScreenFactory
import ru.pasha.feature.home.api.HomeArguments
import ru.pasha.feature.home.api.HomeFeature
import ru.pasha.feature.map.api.MapFeature
import ru.pasha.network.api.ApiFactory
import ru.pasha.network.api.AuthController
import ru.pasha.network.api.NetworkFactory
import ru.pasha.network.api.SessionApi
import ru.pasha.walking.auth.AuthControllerImpl
import ru.pasha.walking.auth.AuthManager
import ru.pasha.walking.auth.AuthManagerImpl
import ru.pasha.walking.auth.EncryptedSessionStorage
import ru.pasha.walking.auth.SessionStorage

@Module
interface ApplicationModule {

    companion object {
        @Provides
        @ApplicationScope
        fun provideCicerone(): Cicerone<Router> = Cicerone.create()

        @Provides
        @ApplicationScope
        fun provideRouter(cicerone: Cicerone<Router>): Router = cicerone.router

        @Provides
        @StartScreen
        @ApplicationScope
        fun provideStartScreen(feature: HomeFeature): Screen = feature.getHomeScreen(
            HomeArguments(route = null)
        )

        @SuppressLint("HardwareIds")
        @Provides
        @ApplicationScope
        fun provideApiFactory(
            authController: AuthController,
            context: Context
        ): ApiFactory = NetworkFactory.create(
            deviceIdProvider = {
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            },
            versionProvider = {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            },
            localeProvider = {
                context.resources.configuration.locales[0].language
            },
            authController = authController,
        )

        @SuppressLint("HardwareIds")
        @Provides
        @ApplicationScope
        fun provideSessionApi(context: Context): SessionApi {
            return NetworkFactory.createSessionApi(
                deviceIdProvider = {
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                },
                versionProvider = {
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
                },
                localeProvider = {
                    context.resources.configuration.locales[0].language
                },
            )
        }

        @Provides
        @ApplicationScope
        fun provideMasterKey(context: Context): MasterKey {
            return MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        }

        @Provides
        @ApplicationScope
        fun provideSessionPrefs(context: Context, masterKey: MasterKey): SharedPreferences {
            return EncryptedSharedPreferences.create(
                context,
                "session_storage",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        @Provides
        @ApplicationScope
        fun provideAuthSdk(context: Context) = YandexAuthSdk.create(YandexAuthOptions(context))

        @Provides
        fun provideMapProvider(
            mapFeature: MapFeature,
            mapFragmentFactory: FragmentFactory,
        ): WalkingMapProvider {
            return object : WalkingMapProvider {
                override val fragmentFactory: FragmentFactory get() = mapFragmentFactory
                override fun mapFragmentScreen(): Screen = mapFeature.getMapScreen()
                override val mapController: MapController = mapFeature.mapController
            }
        }
    }

    @Binds
    @ApplicationScope
    fun bindSessionStorage(sessionStorageImpl: EncryptedSessionStorage): SessionStorage

    @Binds
    @ApplicationScope
    fun bindScreenFactory(screenFactory: ScreenFactory): FragmentFactory

    @Binds
    @ApplicationScope
    fun bindAuthComponent(impl: AuthManagerImpl): AuthManager

    @Binds
    @ApplicationScope
    fun bindAuthManager(authControllerImpl: AuthControllerImpl): AuthController
}
