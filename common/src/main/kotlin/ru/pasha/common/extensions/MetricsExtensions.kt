package ru.pasha.common.extensions

import android.content.res.Resources
import kotlin.math.roundToInt

val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Int.dpToPxF: Float get() = this * Resources.getSystem().displayMetrics.density

val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).roundToInt()

val Int.pxToDpF: Float get() = this / Resources.getSystem().displayMetrics.density

val Int.pxToSp: Int get() = (this / Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

val Int.pxToSpF: Float get() = this / Resources.getSystem().displayMetrics.scaledDensity

val Int.spToPx: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

val Int.spToPxF: Float get() = this * Resources.getSystem().displayMetrics.scaledDensity

val Float.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Float.dpToPxF: Float get() = this * Resources.getSystem().displayMetrics.density

val Float.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).roundToInt()

val Float.pxToDpF: Float get() = this / Resources.getSystem().displayMetrics.density

val Float.pxToSp: Int get() = (this / Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

val Float.pxToSpF: Float get() = this / Resources.getSystem().displayMetrics.scaledDensity

val Float.spToPx: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

val Float.spToPxF: Float get() = this * Resources.getSystem().displayMetrics.scaledDensity
