package ru.pasha.feature.map.internal.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import ru.pasha.common.pattern.BaseFragment
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.map.databinding.MapFragmentBinding
import ru.pasha.feature.map.internal.MapControllerProvider
import javax.inject.Inject

internal class MapFragment @Inject constructor(
    private val viewModelFactory: MapViewModel.Factory,
    private val mapControllerProvider: MapControllerProvider,
) : BaseFragment<MapViewState, MapViewModel, MapFragmentBinding>(
    viewModelClass = MapViewModel::class.java
) {
    private var mapListener: MapListener? = null

    override fun createViewModel(): MapViewModel = viewModelFactory.create()

    override fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        return insets
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

                val aprelevkaLatitude = 55.5514
                val aprelevkaLongitude = 37.0654
                val startZoomLevel = 15.0

                controller.setZoom(startZoomLevel)
                controller.setCenter(GeoPoint(aprelevkaLatitude, aprelevkaLongitude))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = requireActivity().packageName
        Configuration.getInstance()
            .load(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapListener()
    }

    override fun render(viewState: MapViewState) = Unit

    override fun consumeSideEffect(effect: SideEffect) = Unit

    private fun getCurrentLocation(): ru.pasha.common.map.GeoPoint {
        return binding.walkingMap.mapCenter.let { point ->
            ru.pasha.common.map.GeoPoint(
                lat = point.latitude.toFloat(),
                lon = point.longitude.toFloat()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.walkingMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.walkingMap.onPause()
    }

    override fun onDestroyView() {
        destroyMapListener()
        super.onDestroyView()
    }

    private fun initMapListener() {
        mapListener = object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                mapControllerProvider.pointsSourceFlow.tryEmit(getCurrentLocation())
                return false
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                mapControllerProvider.pointsSourceFlow.tryEmit(getCurrentLocation())
                return false
            }
        }.also { binding.walkingMap.addMapListener(it) }
    }

    private fun destroyMapListener() {
        binding.walkingMap.removeMapListener(mapListener)
        mapListener = null

        binding.walkingMap.onDetach()
    }
}
