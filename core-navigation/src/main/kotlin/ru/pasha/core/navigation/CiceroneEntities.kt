package ru.pasha.core.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import ru.pasha.common.pattern.ScreenArguments
import ru.pasha.common.pattern.addScreenArgs

interface FragmentScreen : com.github.terrakok.cicerone.androidx.FragmentScreen {
    override val clearContainer: Boolean get() = true

    companion object {
        operator fun invoke(
            key: String? = null,
            clearContainer: Boolean = true,
            screenArguments: ScreenArguments? = null,
            fragmentClass: Class<out Fragment>,
        ) = object : FragmentScreen {
            override val screenKey = key ?: fragmentClass::class.java.name
            override val clearContainer = clearContainer
            override fun createFragment(factory: FragmentFactory) =
                factory.instantiate(ClassLoader.getSystemClassLoader(), fragmentClass.name).apply {
                    addScreenArgs(screenArguments)
                }
        }
    }
}

interface ActivityScreen : com.github.terrakok.cicerone.androidx.ActivityScreen {
    override val startActivityOptions: Bundle? get() = null

    companion object {
        operator fun invoke(
            key: String? = null,
            startActivityOptions: Bundle? = null,
            intent: Intent,
        ) = object : ActivityScreen {
            override val screenKey = key ?: intent::class.java.name
            override val startActivityOptions = startActivityOptions
            override fun createIntent(context: Context): Intent = Intent()
        }
    }
}
