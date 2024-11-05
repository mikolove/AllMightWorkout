package com.mikolove.auth.domain

import com.mikolove.core.domain.util.DataError

enum class CredentialError : DataError{
    CREATE_CANCELATION,
    CREATE_EXCEPTION,
    GET_CANCELATION,
    GET_EXCEPTION,
    NOT_EXIST
}