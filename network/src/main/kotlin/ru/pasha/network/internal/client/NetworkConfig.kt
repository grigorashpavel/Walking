package ru.pasha.network.internal.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
private val serializationFactory = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    explicitNulls = false
}

internal data class NetworkConfig(
    val baseUrl: String,
    val connectTimeout: Long,
    val writeTimeout: Long,
    val readTimeout: Long,
    val connectionPool: ConnectionPool,
    val dispatcher: Dispatcher,
    val converterFactory: Converter.Factory
)

@Suppress("MagicNumber")
internal fun createNetworkConfig() = NetworkConfig(
    baseUrl = "https://walking-app.ru/api",
    connectTimeout = 15L,
    writeTimeout = 15L,
    readTimeout = 15L,
    connectionPool = ConnectionPool(
        maxIdleConnections = 10,
        keepAliveDuration = 10L,
        timeUnit = TimeUnit.MINUTES
    ),
    dispatcher = Dispatcher().apply {
        maxRequests = 20
        maxRequestsPerHost = 20
    },
    converterFactory = serializationFactory.asConverterFactory("application/json".toMediaType())
)
