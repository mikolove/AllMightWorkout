package com.mikolove.core.domain.loading

import com.mikolove.core.domain.util.DataError

enum class LoadingError : DataError{
    WORKOUT_TYPE_NOT_FOUND,
    GROUP_NOT_FOUND,
}