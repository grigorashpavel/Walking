package ru.pasha.network.api

import android.content.Context
import kotlinx.coroutines.flow.Flow
import ru.pasha.network.internal.connection.ConnectionTrackerImpl

object ConnectionTrackerFactory {
    fun create(context: Context): ConnectionTracker = ConnectionTrackerImpl(context)
}

interface ConnectionTracker {
    val hasConnectionFlow: Flow<Boolean>
    val hasConnection: Boolean
}
