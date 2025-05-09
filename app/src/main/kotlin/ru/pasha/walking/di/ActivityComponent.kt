package ru.pasha.walking.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.pasha.common.di.ActivityScope
import ru.pasha.feature.banner.api.BannerDependencies
import ru.pasha.walking.MainActivity
import ru.pasha.walking.di.features.FeaturesModule

@Component(
    modules = [
        ActivityModule::class,
        FeaturesModule::class
    ]
)
@ActivityScope
interface ActivityComponent : BannerDependencies {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ActivityComponent
    }

    fun inject(activity: MainActivity)
}
