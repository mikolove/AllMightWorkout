package com.mikolove.allmightworkout.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavigationRoot(
    navController: NavHostController,
    route : Any,
) {
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