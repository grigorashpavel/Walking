package ru.pasha.network.internal.client

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.pasha.network.BuildConfig
import ru.pasha.network.api.ApiFactory
import java.util.concurrent.TimeUnit

internal class NetworkClient(
    private val config: NetworkConfig,
    private val interceptors: Set<Interceptor>
) {

    private val okHttpClient by lazy {
        OkHttpClient.Builder().apply {
            connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
            writeTimeout(config.writeTimeout, TimeUnit.SECONDS)
            readTimeout(config.readTimeout, TimeUnit.SECONDS)
            connectionPool(config.connectionPool)
            dispatcher(config.dispatcher)

            interceptors.forEach(::addInterceptor)

            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(config.converterFactory)
            .build()
    }

    internal val apiFactory: ApiFactory by lazy { ApiFactory(retrofit) }
}
