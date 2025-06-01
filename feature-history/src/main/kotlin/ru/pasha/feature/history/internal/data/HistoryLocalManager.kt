@file:Suppress("TooGenericExceptionCaught")

package ru.pasha.feature.history.internal.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

internal class HistoryLocalManager @Inject constructor(
    private val context: Context
) {
    private val jsonFormat = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    suspend fun saveRouteToFile(data: Route): Boolean = withContext(Dispatchers.IO) {
        try {
            val jsonString = jsonFormat.encodeToString(data)
            val file = File(context.filesDir, "${data.name}.json")
            file.writeText(jsonString)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun removeRouteFile(name: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = File(context.filesDir, "$name.json")
            file.delete()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun loadDataFromFile(fileName: String): Result<Route> = withContext(Dispatchers.IO) {
        try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) throw FileNotFoundException("File $fileName not found")

            val jsonString = file.readText()
            val result = jsonFormat.decodeFromString(Route.serializer(), jsonString)
            Result.success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getSavedRoutes(): List<Route>? = withContext(Dispatchers.IO) {
        try {
            context.filesDir.list()
                ?.filter { it.endsWith(".json") }
                ?.mapNotNull { loadDataFromFile(it).getOrNull() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
