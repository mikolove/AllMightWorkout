package com.mikolove.allmightworkout.framework.presentation.main.loading

enum class LoadingStep(val loadingMessage : String) {
    INIT("Please connect to your account"),
    LOAD_USER("'Loading user"),
    LOADED_USER_CREATE("New user successfully created"),
    LOADED_USER_SYNC("User successfully synchronized"),
    LAUNCH_SYNC("Starting workouts sync"),
    GO_TO_APP("Go to app")
}
