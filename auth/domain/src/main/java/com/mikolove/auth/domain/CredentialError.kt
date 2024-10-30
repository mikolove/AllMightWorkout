package com.mikolove.auth.domain

import com.mikolove.core.domain.util.Error

enum class CredentialError : Error{
    CREATE_CANCELATION,
    CREATE_EXCEPTION,
    GET_CANCELATION,
    GET_EXCEPTION,
    NOT_EXIST
}