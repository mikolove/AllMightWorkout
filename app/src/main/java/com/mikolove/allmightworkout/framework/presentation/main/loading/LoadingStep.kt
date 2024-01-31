package com.mikolove.allmightworkout.framework.presentation.main.loading

enum class LoadingStep(val loadingMessage : String) {
    FIRST_LAUNCH("Initializing"),
    NOT_CONNECTED("Please connect to your account"),
    CONNECTED("User connected"),
    LOAD_USER("'Loading user"),
    LAUNCH_SYNC("Starting workouts sync"),
    GO_TO_APP("Go to app")
}
