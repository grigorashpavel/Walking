package ru.pasha.common.extensions

import android.content.Context
import android.content.res.Configuration

fun Context.isNightMode(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
