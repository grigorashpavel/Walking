package ru.pasha.walking.di

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentFactory
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.pasha.common.di.ActivityScope
import ru.pasha.core.navigation.NavigationApi
import ru.pasha.core.navigation.ScreenFactory
import ru.pasha.feature.banner.api.BannerFeature
import ru.pasha.walking.auth.EncryptedSessionStorage
import ru.pasha.walking.auth.SessionStorage

@Module
interface ActivityModule {
    companion object {
        @Provides
        @ActivityScope
        fun provideNavigationApi(cicerone: Cicerone<Router>): NavigationApi = object :
            NavigationApi {
            override fun getRouter(): Router = cicerone.router

            override fun setNavigator(navigator: AppNavigator) {
                cicerone.getNavigatorHolder().setNavigator(navigator)
            }

            override fun removeNavigator() {
                cicerone.getNavigatorHolder().removeNavigator()
            }
        }

        @Provides
        @ActivityScope
        fun provideMasterKey(context: Context): MasterKey {
            return MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        }

        @Provides
        @ActivityScope
        fun provideSessionPrefs(context: Context, masterKey: MasterKey): SharedPreferences {
            return EncryptedSharedPreferences.create(
                context,
                "SessionStorage",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        @Provides
        @ActivityScope
        fun provideAuthSdk(context: Context) = YandexAuthSdk.create(YandexAuthOptions(context))

        @Provides
        @ActivityScope
        @StartScreen
        fun provideStartScreen(feature: BannerFeature): Screen = feature.getFeaturePreviewScreen()
    }

    @Binds
    @ActivityScope
    fun bindSessionStorage(sessionStorageImpl: EncryptedSessionStorage): SessionStorage

    @Binds
    @ActivityScope
    fun bindScreenFactory(screenFactory: ScreenFactory): FragmentFactory
}
