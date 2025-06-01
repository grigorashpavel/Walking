package ru.pasha.feature.history.api

import android.content.Context
import ru.pasha.common.di.WalkingMapProvider
import ru.pasha.network.api.ApiFactory

interface HistoryDependencies {
    val mapProvider: WalkingMapProvider
    val apiFactory: ApiFactory
    val context: Context
}
