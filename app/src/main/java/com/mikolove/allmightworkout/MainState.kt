package com.mikolove.allmightworkout

import com.mikolove.allmightworkout.framework.presentation.network.NetworkStatus
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class MainState(

    val isWorkoutTypesChecked : Boolean = false,

    val isLoggedIn : Boolean = false,

    val isCheckingAuth : Boolean = false,

    val isLoadingData : Boolean = false
)