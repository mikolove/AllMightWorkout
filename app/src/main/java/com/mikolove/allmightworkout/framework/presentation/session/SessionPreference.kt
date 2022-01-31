package com.mikolove.allmightworkout.framework.presentation.session

data class SessionPreference(
    val logged : SessionLoggedType,
    val lastUserId : String?,
)

enum class SessionLoggedType(val value: String) {
    CONNECTED("connected"),
    DISCONNECTED("disconnected"),
    OFFLINE("offline")
}

fun getSessionLoggedTypeFromValue(value: String?): SessionLoggedType {
    return when(value){
        SessionLoggedType.CONNECTED.value -> SessionLoggedType.CONNECTED
        SessionLoggedType.DISCONNECTED.value -> SessionLoggedType.DISCONNECTED
        SessionLoggedType.OFFLINE.value -> SessionLoggedType.OFFLINE
        else -> SessionLoggedType.DISCONNECTED
    }
}