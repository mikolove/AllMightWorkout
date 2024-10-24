package com.mikolove.core.data.auth

import com.mikolove.core.domain.auth.AuthInfo


fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        userId = userId
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        userId = userId
    )
}