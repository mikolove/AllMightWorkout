package com.mikolove.allmightworkout.business.interactors.main.session

import com.mikolove.allmightworkout.business.interactors.sync.SyncNetworkConnectivity

class SessionInteractors(
    val getSessionPreference : GetSessionPreferences,
    val getSessionConnectivityStatus: SyncNetworkConnectivity
){
}