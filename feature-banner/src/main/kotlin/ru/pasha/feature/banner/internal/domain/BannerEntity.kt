package ru.pasha.feature.banner.internal.domain

import ru.pasha.common.Text
import ru.pasha.common.models.ImageModel

data class BannerEntity(
    val title: Text,
    val subtitle: Text?,
    val image: ImageModel,
)
