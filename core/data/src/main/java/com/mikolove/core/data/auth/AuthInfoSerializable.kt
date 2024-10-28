package com.mikolove.core.data.auth

import kotlinx.serialization.Serializable


@Serializable
data class AuthInfoSerializable(
    val userId: String
)