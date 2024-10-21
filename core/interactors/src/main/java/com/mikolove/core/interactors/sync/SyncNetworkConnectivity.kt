package com.mikolove.core.interactors.sync

import com.mikolove.core.data.network.NetworkStatus
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
class SyncNetworkConnectivity(
    private val connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor
) {

    fun execute() : Flow<DataState<NetworkStatus>> =
        connectivityManagerNetworkMonitor.isOnline.map { isConnected ->

            if(isConnected){
                DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncNetworkConnectivity.Available")
                        .title(SYNCNETWORK_TITLE)
                        .description(SYNCNETWORK_AVAILABLE)
                        .uiComponentType(UIComponentType.None)
                        .messageType(MessageType.Success),
                    data = NetworkStatus.AVAILABLE
                )
            }else{
                DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncNetworkConnectivity.Unavailable")
                        .title(SYNCNETWORK_TITLE)
                        .description(SYNCNETWORK_UNAVAILABLE)
                        .uiComponentType(UIComponentType.None)
                        .messageType(MessageType.Success),
                    data = NetworkStatus.UNAVAILABLE
                )
            }
    }

    companion object{
        val SYNCNETWORK_TITLE = "Connectivity Status"
        val SYNCNETWORK_AVAILABLE = "Network available"
        val SYNCNETWORK_LOSING = "Losing network"
        val SYNCNETWORK_LOST = "Connection lost"
        val SYNCNETWORK_UNAVAILABLE = "Network unavailable"
    }
}

*/
