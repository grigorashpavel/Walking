package ru.pasha.feature.banner.api

import ru.pasha.feature.banner.internal.di.BannerComponent

object BannerFeatureFactory {
    fun create(dependencies: BannerDependencies): BannerFeature =
        BannerComponent.create(dependencies).feature
}
