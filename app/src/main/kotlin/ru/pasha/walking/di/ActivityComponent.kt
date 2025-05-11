package ru.pasha.walking.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.pasha.common.di.ActivityScope
import ru.pasha.walking.MainActivity

@ActivityScope
@Component(modules = [ActivityModule::class])
interface ActivityComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ActivityComponent
    }

    fun inject(activity: MainActivity)
}
