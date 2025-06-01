package ru.pasha.common.pattern

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

interface ScreenArguments : Parcelable

fun <T : ScreenArguments> Fragment.screenArgs(): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) { extractScreenParams() }

@Suppress("DEPRECATION")
fun <T : ScreenArguments> Fragment.extractScreenParams(): T {
    val screenParams = arguments?.getParcelable<T>(this::class.java.name)
    if (screenParams == null) {
        error("ScreenArguments shouldn't be null")
    } else {
        return screenParams
    }
}

fun Fragment.addScreenArgs(screenArgs: ScreenArguments?) {
    if (screenArgs != null) {
        arguments = (arguments ?: Bundle()).apply {
            putParcelable(this@addScreenArgs::class.java.name, screenArgs)
        }
    }
}
