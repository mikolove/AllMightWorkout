package com.mikolove.auth.domain

import com.mikolove.core.domain.util.DataError

enum class AuthError : DataError {
    SAVING_USER,
    CHECKING_USER,
    USER_NOT_EXIST,
    NO_FIREBASE_USER,
}