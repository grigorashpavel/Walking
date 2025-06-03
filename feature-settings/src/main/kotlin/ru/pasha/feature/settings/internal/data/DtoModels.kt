@file:Suppress("Filename", "MatchingDeclarationName")

package ru.pasha.feature.settings.internal.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EndSessionSuccessData(
    @SerialName("message")
    val message: String? = null
)
