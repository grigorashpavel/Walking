package ru.pasha.feature.settings.api

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import ru.pasha.feature.settings.internal.domain.Language
import ru.pasha.feature.settings.internal.domain.LocationTracking
import ru.pasha.feature.settings.internal.domain.StepsTracking
import ru.pasha.feature.settings.internal.domain.Theme
import java.util.Locale

class SettingsManager(applicationContext: Context) {
    private val prefs: SharedPreferences = applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var appTheme: Theme
        get() = Theme.valueOf(
            prefs.getString(THEME_KEY, Theme.SYSTEM_DEFAULT.name) ?: Theme.SYSTEM_DEFAULT.name
        )
        set(value) = prefs.edit { putString(THEME_KEY, value.name) }

    var appLanguage: Language
        get() = Language.valueOf(
            prefs.getString(LANGUAGE_KEY, Language.SYSTEM_DEFAULT.name)
                ?: Language.SYSTEM_DEFAULT.name
        )
        set(value) = prefs.edit { putString(LANGUAGE_KEY, value.name) }

    var locationTrackingEnabled: LocationTracking
        get() = when (prefs.getBoolean(LOCATION_KEY, LocationTracking.DISABLED.value)) {
            true -> LocationTracking.ENABLED
            false -> LocationTracking.DISABLED
        }
        set(value) {
            prefs.edit { putBoolean(LOCATION_KEY, value.value) }
        }

    var stepsTrackingEnabled: StepsTracking
        get() = when (prefs.getBoolean(STEPS_KEY, StepsTracking.DISABLED.value)) {
            true -> StepsTracking.ENABLED
            false -> StepsTracking.DISABLED
        }
        set(value) {
            prefs.edit { putBoolean(STEPS_KEY, value.value) }
        }

    fun applySettings() {
        applyTheme()
        applyLanguage()
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(appTheme.mode)
    }

    private fun applyLanguage() {
        val locale = when (appLanguage) {
            Language.SYSTEM_DEFAULT -> ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]
            else -> Locale(appLanguage.code)
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }

    private companion object {
        const val PREFS_NAME = "application_settings"

        const val THEME_KEY = "theme"
        const val LANGUAGE_KEY = "language"
        const val LOCATION_KEY = "location_tracking"
        const val STEPS_KEY = "steps_tracking"
    }
}
