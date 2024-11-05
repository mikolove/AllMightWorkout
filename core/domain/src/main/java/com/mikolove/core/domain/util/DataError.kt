package com.mikolove.core.domain.util

interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        UNKNOWN,
        EXECUTION_ERROR,
        DISK_FULL,
    }
}