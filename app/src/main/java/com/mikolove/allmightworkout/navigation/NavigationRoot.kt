package com.mikolove.allmightworkout.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.mikolove.allmightworkout.presentation.AmwAppState
import com.mikolove.exercise.presentation.navigation.exercisesGraph

@Composable
fun NavigationRoot(
    appState : AmwAppState,
    route : Any,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = route,
        ) {
        authsGraph(navController)
        homeGraph(navController)
        workoutsGraph(navController)
        exercisesGraph(navController)
    }
}