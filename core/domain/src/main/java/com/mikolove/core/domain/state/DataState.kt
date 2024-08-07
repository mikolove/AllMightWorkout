package com.mikolove.core.domain.state

data class DataState<T>(
    var message: GenericMessageInfo.Builder? = null,
    var data: T? = null,
    val isLoading: Boolean = false
) {

    companion object {

        fun <T> error(
            message: GenericMessageInfo.Builder
        ): DataState<T> {
            return DataState(
                message = message,
                data = null
            )
        }

        fun <T> data(
            message: GenericMessageInfo.Builder? = null,
            data: T? = null,
        ): DataState<T> {
            return DataState(
                message = message,
                data = data
            )
        }

        fun <T> loading(): DataState<T> = DataState(isLoading = true)
    }
}