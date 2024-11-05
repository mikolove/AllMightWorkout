package com.mikolove.core.presentation.ui

import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.Result

fun DataError.asUiText(): UiText {
    return when(this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )
        DataError.Local.EXECUTION_ERROR -> UiText.StringResource(
            R.string.error_execution
        )
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.error_request_timeout
        )
        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.error_no_internet
        )
        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.error_server_error
        )
        DataError.Network.SERIALIZATION -> UiText.StringResource(
            R.string.error_serialization
        )
        else -> UiText.StringResource(R.string.error_unknown)
    }
}

fun Result.Error<DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}