package com.mikolove.allmightworkout.business.interactors.sync

import android.net.ConnectivityManager
import android.net.Network
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.allmightworkout.framework.presentation.network.NetworkStatus
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class SyncNetworkConnectivity(
    private val connectivityManager: ConnectivityManager
) {

    fun execute() : Flow<DataState<NetworkStatus>> = callbackFlow {

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                printLogD("SyncNetworkConnectivity","Network available ${network}")
                trySend(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("SyncNetworkConnectivity.Available")
                            .title(SYNCNETWORK_TITLE)
                            .description(SYNCNETWORK_AVAILABLE)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = NetworkStatus.AVAILABLE
                    )
                )
            }
/*            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("SyncNetworkConnectivity.Losing")
                            .title(SYNCNETWORK_TITLE)
                            .description(SYNCNETWORK_LOSING)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = SessionConnectivityStatus.LOSING
                    )
                )
            }*/

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("SyncNetworkConnectivity.Lost")
                            .title(SYNCNETWORK_TITLE)
                            .description(SYNCNETWORK_LOST)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = NetworkStatus.LOST
                    )
                )
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("SyncNetworkConnectivity.Unavailable")
                            .title(SYNCNETWORK_TITLE)
                            .description(SYNCNETWORK_UNAVAILABLE)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = NetworkStatus.UNAVAILABLE
                    )
                )
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }

    }.distinctUntilChanged()

    companion object{
        val SYNCNETWORK_TITLE = "Connectivity Status"
        val SYNCNETWORK_AVAILABLE = "Network available"
        val SYNCNETWORK_LOSING = "Losing network"
        val SYNCNETWORK_LOST = "Connection lost"
        val SYNCNETWORK_UNAVAILABLE = "Network unavailable"
    }
}

