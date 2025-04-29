package com.mikolove.allmightworkout

data class MainState(

    val isCheckingAuth : Boolean = true,

    val isLoadingData : Boolean = true,

    val isWorkoutTypesChecked : Boolean = false,

    val isLoggedIn : Boolean = false,

)