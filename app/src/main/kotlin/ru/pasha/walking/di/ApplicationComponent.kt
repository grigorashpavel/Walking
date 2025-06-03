package ru.pasha.walking.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.pasha.common.di.ApplicationScope
import ru.pasha.feature.banner.api.BannerDependencies
import ru.pasha.feature.history.api.HistoryDependencies
import ru.pasha.feature.home.api.HomeDependencies
import ru.pasha.feature.map.api.MapDependencies
import ru.pasha.feature.settings.api.SettingsDependencies
import ru.pasha.walking.WalkingApp
import ru.pasha.walking.di.features.FeaturesModule

@CommonFeature
@ApplicationScope
@Component(modules = [ApplicationModule::class, FeaturesModule::class])
interface ApplicationComponent :
    BannerDependencies,
    MapDependencies,
    HomeDependencies,
    HistoryDependencies,
    SettingsDependencies {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(application: WalkingApp)
}
