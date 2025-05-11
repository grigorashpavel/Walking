package ru.pasha.walking

import android.app.Application
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import ru.pasha.common.initImageLoader
import ru.pasha.core.navigation.NavigationHolder
import ru.pasha.walking.di.DaggerApplicationComponent
import ru.pasha.walking.di.StartScreen
import javax.inject.Inject

class WalkingApp : Application(), NavigationHolder {
    override fun onCreate() {
        DaggerApplicationComponent.factory()
            .create(applicationContext)
            .inject(this)

        super.onCreate()

        initImageLoader(applicationContext)
    }

    @Inject
    override lateinit var factory: FragmentFactory

    @Inject
    @StartScreen
    override lateinit var startScreen: Screen
}
