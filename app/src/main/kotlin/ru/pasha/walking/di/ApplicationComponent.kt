package ru.pasha.walking.di

import dagger.Component
import ru.pasha.common.di.ApplicationScope
import ru.pasha.feature.banner.api.BannerDependencies
import ru.pasha.feature.home.api.HomeDependencies
import ru.pasha.feature.map.api.MapDependencies
import ru.pasha.walking.WalkingApp
import ru.pasha.walking.di.features.FeaturesModule

@CommonFeature
@ApplicationScope
@Component(modules = [ApplicationModule::class, FeaturesModule::class])
interface ApplicationComponent : BannerDependencies, MapDependencies, HomeDependencies {
    @Component.Factory
    interface Factory {
        fun create(): ApplicationComponent
    }

    fun inject(application: WalkingApp)
}
