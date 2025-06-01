package ru.pasha.feature.map.internal.presentation

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsCompat
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.pasha.common.R
import ru.pasha.common.map.GeoPoint
import ru.pasha.common.pattern.BaseFragment
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.map.databinding.MapFragmentBinding
import ru.pasha.feature.map.internal.MapControllerProvider
import ru.pasha.feature.map.internal.entity
import ru.pasha.feature.map.internal.point
import javax.inject.Inject

@Suppress("TooManyFunctions")
internal class MapFragment @Inject constructor(
    private val viewModelFactory: MapViewModel.Factory,
    private val mapControllerProvider: MapControllerProvider,
    private val permissionManager: MapPermissionManager,
) : BaseFragment<MapViewState, MapViewModel, MapFragmentBinding>(
    viewModelClass = MapViewModel::class.java
) {
    private var mapListener: MapListener? = null
    private var centerMarker: Marker? = null

    private val markerIcon get() = ResourcesCompat.getDrawable(
        resources, R.drawable.ic_default_marker_32, requireContext().theme
    )

    private val currentMarkers = hashMapOf<Int, Marker>()

    private var route: Polyline? = null

    private val location get() = binding.walkingMap.mapCenter.point()

    private var permissionLauncher: ActivityResultLauncher<Void?>? = null

    private var locationOverlay: MyLocationNewOverlay? = null

    override fun createViewModel(): MapViewModel = viewModelFactory.create()

    override fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat = insets

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().apply {
            userAgentValue = requireActivity().packageName
            load(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE))
            isMapViewHardwareAccelerated = true
        }

        permissionLauncher = permissionManager.registerPermissionLauncher(this) { granted ->
            viewModel.handlePermissionResult(granted)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MapFragmentBinding {
        return MapFragmentBinding.inflate(inflater, container, false).apply {
            with(walkingMap) {
                setTileSource(TileSourceFactory.MAPNIK)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                setMultiTouchControls(true)

                centerMarker = Marker(this).apply {
                    position = mapCenter.point()
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    icon = markerIcon
                }
                overlays.add(centerMarker)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapListener()
    }

    // Рендер можно использовать только для свойств настроек (видимость маркеров и тд, не центр и зум)
    override fun render(viewState: MapViewState) {
        renderCenteredMarker(viewState)
        renderLocation(viewState)
        renderZoom(viewState)
        renderMarkers(viewState)
        renderRoute(viewState)

        binding.walkingMap.invalidate()
    }

    override fun consumeSideEffect(effect: SideEffect) = when (effect) {
        is MapSideEffect.RequestLocationPermission -> {
            permissionLauncher?.launch(null) ?: Unit
        }

        is MapSideEffect.StartListenLocation -> {
            setupLocationOverlay()
            enableLocation()
        }

        is MapSideEffect.StopListenLocation -> disableLication()

        else -> Unit
    }

    private fun setupLocationOverlay() {
        if (locationOverlay != null) return
        val location = MyLocationNewOverlay(
            object : GpsMyLocationProvider(requireContext()) {
                override fun onLocationChanged(location: Location) {
                    mapControllerProvider.updateUserLocation(
                        GeoPoint(
                            lat = location.latitude.toFloat(),
                            lon = location.longitude.toFloat(),
                        )
                    )
                    super.onLocationChanged(location)
                }
            },
            binding.walkingMap,
        ).apply {
            isDrawAccuracyEnabled = false
            setPersonAnchor(0.5f, 0.895f)
            runOnFirstFix {
                activity?.runOnUiThread {
                    locationOverlay?.myLocation?.let(binding.walkingMap.controller::animateTo)
                }
            }
        }
        locationOverlay = location
    }

    private fun enableLocation() {
        locationOverlay?.let {
            if (!binding.walkingMap.overlays.contains(it)) {
                locationOverlay?.let(binding.walkingMap.overlays::add)
                locationOverlay?.isEnabled = true
            }

            locationOverlay?.enableMyLocation()
            locationOverlay?.enableFollowLocation()
            binding.walkingMap.invalidate()
        }
    }

    private fun disableLication() {
        locationOverlay?.disableFollowLocation()
        locationOverlay?.disableMyLocation()
        locationOverlay?.let(binding.walkingMap.overlays::remove)
        locationOverlay?.isEnabled = false
        binding.walkingMap.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.walkingMap.onResume()
        locationOverlay?.enableMyLocation()
    }

    override fun onPause() {
        super.onPause()
        binding.walkingMap.onPause()
        locationOverlay?.disableMyLocation()
    }

    override fun onDestroyView() {
        destroyMap()
        super.onDestroyView()
    }

    private fun renderRoute(viewState: MapViewState) {
        with(binding.walkingMap) {
            if (viewState.route == null) {
                route?.let(overlays::remove)
                route = null
                return
            }
            if (route != null) return

            Polyline(this).apply {
                setPoints(viewState.route)
                outlinePaint.color =
                    ResourcesCompat.getColor(
                        resources,
                        R.color.walking_app_marker_1,
                        requireContext().theme
                    )
                outlinePaint.strokeWidth = ROUTE_WIDTH
            }.let {
                this@MapFragment.route = it
                overlays.add(it)
            }
        }
    }

    private fun renderCenteredMarker(viewState: MapViewState) {
        centerMarker?.setVisible(viewState.isCenteredMarkerVisible)
    }

    private fun renderLocation(viewState: MapViewState) {
        binding.walkingMap.controller.setCenter(viewState.location)
    }

    private fun renderZoom(viewState: MapViewState) {
        binding.walkingMap.controller.setZoom(viewState.zoom)
    }

    private fun renderMarkers(viewState: MapViewState) {
        viewState.markersToRemove.forEach { marker ->
            currentMarkers[marker.id]?.let { founded ->
                currentMarkers.remove(marker.id)
                binding.walkingMap.overlays.remove(founded)
            }
        }
        viewState.markersToDraw.forEach { marker ->
            if (!currentMarkers.containsKey(marker.id)) {
                currentMarkers[marker.id] = createPoiMarker(marker.color).also { m ->
                    binding.walkingMap.overlays.add(m)
                }
            }
        }
    }

    private fun initMapListener() {
        mapListener = object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                mapControllerProvider.setCurrentLocation(location.entity())
                centerMarker?.position = location
                return false
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                mapControllerProvider.setCurrentLocation(location.entity())
                event?.zoomLevel?.let(mapControllerProvider::setZoom)
                centerMarker?.position = location
                return false
            }
        }.also(binding.walkingMap::addMapListener)
    }

    private fun createPoiMarker(@ColorInt color: Int): Marker {
        return Marker(binding.walkingMap).apply {
            position = location
            icon = markerIcon
            icon.setTint(color)
        }
    }

    private fun destroyMap() {
        binding.walkingMap.removeMapListener(mapListener)
        mapListener = null
        binding.walkingMap.onDetach()

        centerMarker?.closeInfoWindow()
        centerMarker = null

        route = null
        currentMarkers.clear()
        locationOverlay = null
    }

    private companion object {
        const val ROUTE_WIDTH = 6f
    }
}
