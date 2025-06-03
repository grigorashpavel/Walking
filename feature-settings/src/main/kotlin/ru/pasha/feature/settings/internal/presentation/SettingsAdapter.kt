@file:Suppress("OptionalUnit")

package ru.pasha.feature.settings.internal.presentation

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import ru.pasha.common.Text
import ru.pasha.common.extensions.setText
import ru.pasha.common.views.BaseRecyclerAdapter
import ru.pasha.feature.settings.R
import ru.pasha.feature.settings.databinding.SettingsItemBinding
import ru.pasha.feature.settings.internal.domain.Language
import ru.pasha.feature.settings.internal.domain.LocationTracking
import ru.pasha.feature.settings.internal.domain.SettingsEntity
import ru.pasha.feature.settings.internal.domain.Theme

internal class SettingsAdapter(
    private val changeLanguageCallback: (Language) -> Unit,
    private val changeThemeCallback: (Theme) -> Unit,
    private val changeLocationOptionCallback: (LocationTracking) -> Unit,
) : BaseRecyclerAdapter<SettingsItemBinding, SettingsEntity>(
    inflateBinding = { inflater, parent, attach ->
        SettingsItemBinding.inflate(inflater, parent, attach)
    }
) {
    override fun bind(binding: SettingsItemBinding, item: SettingsEntity, position: Int?) {
        binding.setupItem(item)
    }

    private fun SettingsItemBinding.setupItem(item: SettingsEntity) = when (item) {
        is SettingsEntity.Language -> {
            settingsTitle.isVisible = false
            itemSwitch.isVisible = false

            icon.setImageResource(R.drawable.ic_language_24)

            spinner.apply {
                adapter = ArrayAdapter.createFromResource(
                    context,
                    R.array.languages_array,
                    R.layout.spinner_item,
                ).apply {
                    setDropDownViewResource(R.layout.spinner_item)
                }
                val currentPos = item.current.toPosition()
                setSelection(currentPos)

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val newLanguage = when (position) {
                            0 -> Language.ENGLISH
                            1 -> Language.RUSSIAN
                            else -> Language.SYSTEM_DEFAULT
                        }
                        if (currentPos != position) changeLanguageCallback(newLanguage)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                }
            }
            Unit
        }
        is SettingsEntity.LocationTracking -> {
            spinner.isVisible = false

            settingsTitle.setText(Text.Resource(ru.pasha.common.R.string.walking_app_listen_location))
            icon.setImageResource(ru.pasha.common.R.drawable.ic_location_24)

            itemSwitch.isChecked = item.enabled
            itemSwitch.setOnCheckedChangeListener { _, isChecked ->
                changeLocationOptionCallback(
                    if (isChecked) LocationTracking.ENABLED else LocationTracking.DISABLED
                )
            }
        }
        is SettingsEntity.Theme -> {
            settingsTitle.isVisible = false
            itemSwitch.isVisible = false

            icon.setImageResource(R.drawable.ic_night_theme_24)

            spinner.apply {
                adapter = ArrayAdapter.createFromResource(
                    context,
                    R.array.theme_array,
                    R.layout.spinner_item,
                ).apply {
                    setDropDownViewResource(R.layout.spinner_item)
                }
                val currentPos = item.current.toPosition()
                setSelection(currentPos)

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val newTheme = when (position) {
                            0 -> Theme.LIGHT
                            1 -> Theme.DARK
                            else -> Theme.SYSTEM_DEFAULT
                        }
                        if (currentPos != position) changeThemeCallback(newTheme)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                }
            }
            Unit
        }
    }

    private fun Language.toPosition() = when (this) {
        Language.ENGLISH -> 0
        Language.RUSSIAN -> 1
        Language.SYSTEM_DEFAULT -> 2
    }

    private fun Theme.toPosition() = when (this) {
        Theme.LIGHT -> 0
        Theme.DARK -> 1
        Theme.SYSTEM_DEFAULT -> 2
    }
}
