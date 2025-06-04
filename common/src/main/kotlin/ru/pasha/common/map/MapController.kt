package ru.pasha.common.map

import kotlinx.coroutines.flow.Flow
import ru.pasha.common.Text

@Suppress("ComplexInterface", "TooManyFunctions")
interface MapController {
    fun setCurrentLocation(point: GeoPoint)
    fun setZoom(zoom: Double)
    fun setCenterMarkerVisibility(show: Boolean)
    fun toggleCreateMarkerFeature(isEnabled: Boolean)
    fun restoreMap()
    fun setWalkingMode(enabled: Boolean)
    fun setPreviewMode(enabled: Boolean)

    val markers: Flow<List<Marker>>
    val route: Flow<Route?>
    fun isReachedMaxMarkers(): Boolean
    suspend fun createMarker(): Marker?
    fun removeMarkers()
    fun removeMarker(marker: Marker)
    suspend fun buildRoute(name: String?): Text?
    fun setRoute(route: Route)
    fun switchLocationListen(enabled: Boolean)
    fun switchStepsListen(enabled: Boolean)
    val stepsFlow: Flow<Int>
}
