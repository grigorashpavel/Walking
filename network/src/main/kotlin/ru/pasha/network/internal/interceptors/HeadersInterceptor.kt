package ru.pasha.network.internal.interceptors

import okhttp3.Interceptor
import ru.pasha.network.internal.Header

internal fun createHeadersInterceptor(headers: Set<Header>) = Interceptor { chain ->
    val request = chain.request().newBuilder().apply {
        headers.forEach { addHeader(it.type.value, it.headerProvider()) }
    }.build()

    chain.proceed(request)
}
