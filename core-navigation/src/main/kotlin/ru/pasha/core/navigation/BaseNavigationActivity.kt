package ru.pasha.core.navigation

import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.androidx.AppNavigator

abstract class BaseNavigationActivity : AppCompatActivity() {
    private val cicerone by lazy { Cicerone.create() }

    protected val router by lazy { cicerone.router }

    protected abstract val navigator: AppNavigator

    override fun onResumeFragments() {
        super.onResumeFragments()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.getNavigatorHolder().removeNavigator()
    }
}
