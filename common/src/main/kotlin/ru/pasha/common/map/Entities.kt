package ru.pasha.common.map

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class GeoPoint(val lat: Float, val lon: Float) : Parcelable

data class Marker(val id: Int, val point: GeoPoint, @ColorInt val color: Int)

@Parcelize
data class Route(
    val id: UUID,
    val name: String,
    val path: List<GeoPoint>,
    val length: Double
) : Parcelable {
    override fun equals(other: Any?): Boolean = (other as? Route)?.id?.let { it == id } ?: false
    override fun hashCode(): Int = id.hashCode()
}
