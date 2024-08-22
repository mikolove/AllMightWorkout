package com.mikolove.allmightworkout

sealed class MainEvents{

    object GetNetworkStatus : MainEvents()

    object OnRemoveHeadFromQueue : MainEvents()
}