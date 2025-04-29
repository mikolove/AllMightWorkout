package com.mikolove.allmightworkout.framework.presentation.network

sealed class NetworkEvents {
    object GetNetworkStatus : NetworkEvents()

    object OnRemoveHeadFromQueue: NetworkEvents()
}