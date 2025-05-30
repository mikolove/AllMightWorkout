package com.mikolove.workout.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.mikolove.workout.presentation.overview.WorkoutScreenRoot
import kotlinx.serialization.Serializable

@Serializable object WorkoutListRoute
@Serializable object WorkoutGroupRoute
@Serializable object WorkoutSearchRoute
@Serializable data class WorkoutUpsertRoute(val idWorkout: String)
@Serializable data class WorkoutDetailRoute(val idWorkout: String)
@Serializable data class WorkoutAddExerciseRoute(val idWorkout: String)


@Serializable  object WorkoutsRoute

fun NavController.navigateToWorkoutDetail(idWorkout : String, navOptions: NavOptionsBuilder.() -> Unit = {}){
    navigate(route = WorkoutDetailRoute(idWorkout)){
        navOptions
    }
}

fun NavController.navigateToWorkoutAddExerciseRoute(idWorkout : String, navOptions: NavOptionsBuilder.() -> Unit = {}){
    navigate(route = WorkoutAddExerciseRoute(idWorkout)){
        navOptions
    }
}

fun NavController.navigateToWorkoutUpsertRoute(idWorkout : String, navOptions: NavOptionsBuilder.() -> Unit = {}){
    navigate(route = WorkoutUpsertRoute(idWorkout)){
        navOptions
    }
}

fun NavController.navigateToWorkoutGroup(navOptions: NavOptions) =
    navigate(WorkoutGroupRoute,navOptions)

fun NavController.navigateToWorkout(navOptions: NavOptions) =
    navigate(WorkoutListRoute, navOptions)

fun NavController.navigateToWorkoutSearch(navOptions: NavOptions) =
    navigate(WorkoutSearchRoute,navOptions)

fun NavGraphBuilder.workoutsGraph(
    navController: NavController,
){
    navigation<WorkoutsRoute>(startDestination = WorkoutListRoute){

        composable<WorkoutListRoute>{
            WorkoutScreenRoot(
                onSearchClick = {
                    navController.navigateToWorkoutSearch(navOptions {})
                },
                onAddGroupClick = {
                    navController.navigateToWorkoutGroup(navOptions {})
                },
                onWorkoutClick = {
                    navController.navigateToWorkoutDetail(it)
                },
                onUpsertWorkoutClick = {
                    navController.navigateToWorkoutUpsertRoute(it)
                },
            )
        }

        composable<WorkoutUpsertRoute> {  }

        composable<WorkoutSearchRoute> {  }

        composable<WorkoutDetailRoute> {  }

        composable<WorkoutAddExerciseRoute> {  }

        composable<WorkoutGroupRoute> {  }

    }
}