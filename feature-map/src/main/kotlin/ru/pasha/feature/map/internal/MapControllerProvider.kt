package ru.pasha.feature.map.internal

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import ru.pasha.common.map.GeoPoint
import ru.pasha.feature.map.api.MapController
import ru.pasha.feature.map.internal.di.MapScope
import javax.inject.Inject

@MapScope
internal class MapControllerProvider @Inject constructor() : MapController {
    val pointsSourceFlow = MutableSharedFlow<GeoPoint>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val pointsFlow: Flow<GeoPoint> = pointsSourceFlow.filterNotNull()

    override fun getCurrentLocation(): GeoPoint = GeoPoint(2f, 2f)
}
