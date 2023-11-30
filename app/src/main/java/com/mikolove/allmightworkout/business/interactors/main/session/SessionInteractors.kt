package com.mikolove.allmightworkout.business.interactors.main.session

import com.mikolove.allmightworkout.business.interactors.main.auth.GetAuthState
import com.mikolove.allmightworkout.business.interactors.main.auth.SignOut
import com.mikolove.allmightworkout.business.interactors.sync.SyncNetworkConnectivity

class SessionInteractors(
    val signOut: SignOut,
    val getAuthState: GetAuthState,
    val getSessionConnectivityStatus: SyncNetworkConnectivity
){
}