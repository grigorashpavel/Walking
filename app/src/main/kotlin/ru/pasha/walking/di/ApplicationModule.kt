package ru.pasha.walking.di

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.pasha.common.di.ApplicationScope
import ru.pasha.core.navigation.ScreenFactory
import ru.pasha.feature.home.api.HomeFeature

@Module
interface ApplicationModule {

    companion object {
        @Provides
        @StartScreen
        @ApplicationScope
        fun provideStartScreen(feature: HomeFeature): Screen = feature.getHomeScreen()
    }

    @Binds
    @ApplicationScope
    fun bindScreenFactory(screenFactory: ScreenFactory): FragmentFactory
}
