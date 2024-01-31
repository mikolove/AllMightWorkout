package com.mikolove.allmightworkout.framework.presentation.session

data class SessionPreference(
    val logged : SessionLoggedType,
    val lastUserId : String?,
)

enum class SessionLoggedType(val value: String) {
    CONNECTED("connected"),
    DISCONNECTED("disconnected")
}

fun getSessionLoggedType(value: String?): SessionLoggedType {
    return when(value){
        SessionLoggedType.CONNECTED.value -> SessionLoggedType.CONNECTED
        SessionLoggedType.DISCONNECTED.value -> SessionLoggedType.DISCONNECTED
        else -> SessionLoggedType.DISCONNECTED
    }
}