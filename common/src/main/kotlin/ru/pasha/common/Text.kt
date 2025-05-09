package ru.pasha.common

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class Text : Parcelable {

    @Parcelize
    data object Empty : Text()

    @Parcelize
    data class Constant(val text: CharSequence) : Text()

    @Parcelize
    data class Resource(
        @StringRes
        val stringResId: Int
    ) : Text()
}

fun Text?.orEmpty(): Text = this ?: Text.Empty

fun Text.format(context: Context): CharSequence = when (this) {
    is Text.Empty -> ""
    is Text.Constant -> text
    is Text.Resource -> context.getString(stringResId)
}
