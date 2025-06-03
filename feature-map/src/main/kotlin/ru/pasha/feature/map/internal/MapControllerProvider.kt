package ru.pasha.feature.map.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.pasha.common.Text
import ru.pasha.common.map.GeoPoint
import ru.pasha.common.map.MapController
import ru.pasha.common.map.Marker
import ru.pasha.common.map.Route
import ru.pasha.feature.map.api.MapSettingsProvider
import ru.pasha.feature.map.internal.data.RouteRepository
import ru.pasha.feature.map.internal.data.entity
import ru.pasha.feature.map.internal.di.MapScope
import ru.pasha.feature.map.internal.presentation.MapState
import javax.inject.Inject

@MapScope
@Suppress("TooManyFunctions")
internal class MapControllerProvider @Inject constructor(
    private val settingsProvider: MapSettingsProvider,
    private val repository: RouteRepository,
) : MapController {
    val controllerFlow = MutableStateFlow(createDefaultState())

    var locationCallback: (Boolean) -> Unit = {}

    private var userLocation: GeoPoint? = null

    override val route: Flow<Route?> = controllerFlow
        .distinctUntilChangedBy { it.route }
        .map { it.route }

    override fun setCurrentLocation(point: GeoPoint) {
        update { copy(center = point) }
    }

    override fun setZoom(zoom: Double) {
        update { copy(zoom = zoom) }
    }

    override fun setCenterMarkerVisibility(show: Boolean) {
        update { copy(centerMarkerVisibility = show) }
    }

    override fun toggleCreateMarkerFeature(isEnabled: Boolean) {
        update { copy(createMarkerOptionEnabled = isEnabled) }
    }

    override fun restoreMap() {
        update { createDefaultState() }
    }

    override fun setWalkingMode(enabled: Boolean) {
        update { copy(walkingModeEnabled = enabled) }
    }

    override fun setPreviewMode(enabled: Boolean) {
        update { copy(previewModeEnabled = enabled) }
    }

    override val markers: Flow<List<Marker>> = controllerFlow
        .distinctUntilChangedBy { it.createdMarkers }
        .map { it.createdMarkers }

    override suspend fun createMarker(): Marker? {
        with(controllerFlow.value) {
            val location = center
            val markerPosition = createdMarkers.size
            if (markerPosition >= settingsProvider.maxPois) return null

            val markerColor = settingsProvider.markersColor[markerPosition]
            return repository.getNearestPoint(location).getOrNull()?.point?.let { point ->
                Marker(id = markerColor.hashCode(), point = point.entity(), color = markerColor)
            }?.also {
                update {
                    copy(
                        createdMarkers = createdMarkers.toMutableList().apply { add(it) },
                        center = it.point,
                    )
                }
            }
        }
    }

    override fun isReachedMaxMarkers(): Boolean =
        controllerFlow.value.createdMarkers.size >= settingsProvider.maxPois

    override fun removeMarkers() {
        update { copy(createdMarkers = emptyList()) }
    }

    override fun switchLocationListen(enabled: Boolean) {
        if (!enabled) userLocation = null
        locationCallback(enabled)
    }

    override fun setRoute(route: Route) {
        update { copy(route = route) }
    }

    override suspend fun buildRoute(name: String?): Text? {
        val userNearPoint = userLocation?.let { repository.getNearestPoint(it) }
        return repository.buildRoute(
            pois = buildList {
                userNearPoint?.getOrNull()?.point?.entity()?.let(::add)
                controllerFlow.value.createdMarkers.map { it.point }.let(::addAll)
            },
            name = name
        ).fold(
            onSuccess = { response ->
                update { copy(route = response.route.entity()) }
                null
            },
            onFailure = {
                it.message?.let(Text::Constant)
            }
        )
    }

    override fun removeMarker(marker: Marker) {
        update {
            copy(
                createdMarkers = createdMarkers.toMutableList().apply { remove(marker) }
            )
        }
    }

    fun updateUserLocation(location: GeoPoint) {
        userLocation = location
    }

    fun createDefaultState(): MapState {
        return MapState(
            settingsProvider.startZoom,
            settingsProvider.startLocation,
            settingsProvider.isCenterMarkerVisible,
            createMarkerOptionEnabled = false,
            createdMarkers = listOf(),
            route = null,
            walkingModeEnabled = false,
            previewModeEnabled = false,
        )
    }

    private fun update(transformer: MapState.() -> MapState) {
        controllerFlow.update(transformer)
    }
}
