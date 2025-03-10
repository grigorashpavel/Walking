package ru.pasha.network.internal

internal enum class HeaderType(val value: String) {
    SESSION_ID("X-Session-Id"),
    IDEMPOTENCY_KEY("X-Idempotency-Key"),
    DEVICE_ID("X-Device-Id"),
    CLIENT_VERSION("X-Client-Version"),
    REQUEST_LANGUAGE("X-Request-Language"),
}

internal data class Header(val type: HeaderType, val headerProvider: () -> String)
