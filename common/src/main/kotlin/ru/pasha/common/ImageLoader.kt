package ru.pasha.common

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.ImageRequest
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import ru.pasha.common.models.ImageModel

private var imageLoader: ImageLoader? = null

fun initImageLoader(context: Context) {
    imageLoader = ImageLoader.Builder(context)
        .diskCache(getImageDiskCache(context))
        .build()
}

enum class ScaleType {
    MATRIX,
    FIT_XY,
    FIT_START,
    FIT_CENTER,
    FIT_END,
    CENTER,
    CENTER_CROP,
    CENTER_INSIDE;

    fun toAndroidType() = when (this) {
        MATRIX -> ImageView.ScaleType.MATRIX
        FIT_XY -> ImageView.ScaleType.FIT_XY
        FIT_START -> ImageView.ScaleType.FIT_START
        FIT_CENTER -> ImageView.ScaleType.FIT_CENTER
        FIT_END -> ImageView.ScaleType.FIT_END
        CENTER -> ImageView.ScaleType.CENTER
        CENTER_CROP -> ImageView.ScaleType.CENTER_CROP
        CENTER_INSIDE -> ImageView.ScaleType.CENTER_INSIDE
    }
}

private fun ImageView.loadImage(
    url: String?,
    scaleType: ScaleType = ScaleType.FIT_XY,
    @DrawableRes placeholder: Int? = null,
    builder: ImageRequest.Builder.() -> Unit = {},
    resultCallback: (loaded: Boolean) -> Unit = {}
) {
    this.scaleType = scaleType.toAndroidType()

    val request = ImageRequest.Builder(context)
        .data(url)
        .scale(Scale.FIT)
        .target(this)
        .apply { placeholder?.let { placeholder(it) } }
        .apply(builder)
        .allowHardware(true)
        .fetcherDispatcher(Dispatchers.IO)
        .listener(
            onSuccess = { _, _ -> resultCallback(true) },
            onError = { _, _ -> resultCallback(false) }
        )
        .build()

    imageLoader!!.enqueue(request)
}

fun ImageView.loadImage(model: ImageModel, resultCallback: (loaded: Boolean) -> Unit = {}) {
    val url = when (model) {
        is ImageModel.Res -> "android.resource://${context.packageName}/${model.id}"
        is ImageModel.Url -> model.url
    }
    loadImage(
        url = url,
        scaleType = model.scaleType,
        placeholder = model.placeholder,
        resultCallback = resultCallback,
    )
}

private const val MAX_CACHE_PERCENT = 0.0025

private fun getImageDiskCache(context: Context) = DiskCache.Builder()
    .directory(context.cacheDir.resolve("images_cache"))
    .maxSizePercent(MAX_CACHE_PERCENT)
    .build()
