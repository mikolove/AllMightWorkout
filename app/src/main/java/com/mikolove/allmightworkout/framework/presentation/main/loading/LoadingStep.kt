package com.mikolove.allmightworkout.framework.presentation.main.loading

enum class LoadingStep(val loadingMessage : String) {
    INIT("Please connect to your account"),
    LOADING(""),
    LOAD_USER("Loading user info"),
    LOADED_USER_CREATE("New user successfully created"),
    LOADED_USER_SYNC("User successfully synchronized"),
    LAUNCH_TOTAL_SYNC("Starting workouts sync"),
    SYNC_WKT_AND_BP("Sync workouts types and body parts"),
    SYNC_DELETED_EXERCISESETS("Sync deleted exercises sets"),
    SYNC_DELETED_EXERCISES("Sync deleted exercises"),
    SYNC_DELETED_WORKOUTS("Sync deleted workouts"),
    SYNC_HISTORY("Sync history"),
    SYNC_EXERCISES("Sync exercises"),
    SYNC_WORKOUTS("Sync workouts"),
    SYNC_WORKOUTS_EXERCISES("Sync workouts and exercises"),
    GO_TO_APP("")
}
