package ru.pasha.feature.settings.internal.domain

import androidx.appcompat.app.AppCompatDelegate

enum class Theme(val mode: Int) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM_DEFAULT(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}

enum class Language(val code: String) {
    ENGLISH("en"),
    RUSSIAN("ru"),
    SYSTEM_DEFAULT("")
}

enum class LocationTracking(val value: Boolean) {
    ENABLED(true),
    DISABLED(false)
}

enum class StepsTracking(val value: Boolean) {
    ENABLED(true),
    DISABLED(false)
}
