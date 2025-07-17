package com.mikolove.allmightworkout.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.mikolove.allmightworkout.presentation.AmwAppState
import com.mikolove.exercise.presentation.navigation.exercisesGraph
import com.mikolove.workout.presentation.navigation.workoutsGraph

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationRoot(
    appState : AmwAppState,
    route : Any,
    sharedTransitionScope: SharedTransitionScope
) {
    val navController = appState.navController

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = route,
        ) {
            authsGraph(navController)
            homeGraph(navController)
            workoutsGraph(
                navController,
                sharedTransitionScope)
            exercisesGraph(navController)
        }
    }

}