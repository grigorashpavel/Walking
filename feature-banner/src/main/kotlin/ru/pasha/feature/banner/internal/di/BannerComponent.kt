package ru.pasha.feature.banner.internal.di

import dagger.Component
import ru.pasha.feature.banner.api.BannerDependencies
import ru.pasha.feature.banner.api.BannerFeature

@Component(
    dependencies = [BannerDependencies::class],
    modules = [BannerModule::class]
)
@BannerScope
interface BannerComponent {
    val feature: BannerFeature

    companion object {
        fun create(dependencies: BannerDependencies): BannerComponent =
            DaggerBannerComponent.factory().create(dependencies)
    }

    @Component.Factory
    interface Factory {
        fun create(dependencies: BannerDependencies): BannerComponent
    }
}
