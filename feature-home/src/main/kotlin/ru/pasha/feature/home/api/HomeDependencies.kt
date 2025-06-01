package ru.pasha.feature.home.api

import ru.pasha.common.di.WalkingMapProvider

interface HomeDependencies {
    val mapProvider: WalkingMapProvider
}
