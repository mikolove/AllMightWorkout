package com.mikolove.allmightworkout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mikolove.workout.presentation.WorkoutScreenRoot
import kotlinx.serialization.Serializable

@Serializable
object WorkoutListRoute

@Serializable
object WorkoutsRoute

fun NavController.navigateToWorkout(navOptions: NavOptions) =
    navigate(WorkoutListRoute, navOptions)

fun NavGraphBuilder.workoutsGraph(
    navController: NavController,
){
    navigation<WorkoutsRoute>(startDestination = WorkoutListRoute){

        composable<WorkoutListRoute>{
            WorkoutScreenRoot()
        }
    }
}