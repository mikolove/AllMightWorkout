@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mikolove.workout.presentation.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.mikolove.workout.presentation.detail.WorkoutDetailScreenRoot
import com.mikolove.workout.presentation.overview.WorkoutScreenRoot
import com.mikolove.workout.presentation.upsert.WorkoutUpsertScreenRoot
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
    sharedTransitionScope: SharedTransitionScope,
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
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable
            )
        }

        composable<WorkoutUpsertRoute> {
            WorkoutUpsertScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<WorkoutSearchRoute> {  }

        composable<WorkoutDetailRoute> {
            WorkoutDetailScreenRoot(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable
            )
        }

        composable<WorkoutAddExerciseRoute> {  }

        composable<WorkoutGroupRoute> {  }

    }
}