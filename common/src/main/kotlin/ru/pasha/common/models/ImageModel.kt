package ru.pasha.common.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import ru.pasha.common.ScaleType

@Parcelize
sealed interface ImageModel : Parcelable {
    val scaleType: ScaleType
    val placeholder: Int?

    @Parcelize
    data class Url(
        val url: String,
        override val scaleType: ScaleType = ScaleType.FIT_XY,
        @DrawableRes override val placeholder: Int? = null,
    ) : ImageModel

    @Parcelize
    data class Res(
        @DrawableRes val id: Int,
        override val scaleType: ScaleType = ScaleType.FIT_XY,
        @DrawableRes override val placeholder: Int? = null,
    ) : ImageModel
}
