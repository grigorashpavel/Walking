package ru.pasha.walking.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.pasha.common.di.ActivityScope
import ru.pasha.walking.auth.EncryptedSessionStorage
import ru.pasha.walking.auth.SessionStorage

@Module
interface ActivityModule {
    companion object {
        @Provides
        @ActivityScope
        fun provideCicerone(): Cicerone<Router> = Cicerone.create()

        @Provides
        @ActivityScope
        fun provideRouter(cicerone: Cicerone<Router>): Router = cicerone.router

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
                "session_storage",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        @Provides
        @ActivityScope
        fun provideAuthSdk(context: Context) = YandexAuthSdk.create(YandexAuthOptions(context))
    }

    @Binds
    @ActivityScope
    fun bindSessionStorage(sessionStorageImpl: EncryptedSessionStorage): SessionStorage
}
