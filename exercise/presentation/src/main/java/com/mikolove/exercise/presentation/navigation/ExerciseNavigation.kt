package com.mikolove.exercise.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mikolove.exercise.presentation.detail.ExerciseDetailScreenRoot
import com.mikolove.exercise.presentation.overview.ExerciseScreenRoot
import kotlinx.serialization.Serializable

@Serializable object ExercisesRoute

@Serializable object ExerciseListRoute
@Serializable data class ExerciseDetailRoute(val id : String)

fun NavController.navigateToExerciseDetail(idExercise : String, navOptions: NavOptionsBuilder.() -> Unit = {}){
    navigate(route = ExerciseDetailRoute(idExercise)){
        navOptions
    }
}

fun NavController.navigateToExercise(navOptions: NavOptions) =
    navigate(ExerciseListRoute, navOptions)

fun NavGraphBuilder.exercisesGraph(
    navController: NavController,
){
    navigation<ExercisesRoute>(startDestination = ExerciseListRoute){

        composable<ExerciseListRoute>{
            ExerciseScreenRoot(
                onUpsertExerciseClick = {
                    navController.navigateToExerciseDetail(it)
                },
            )
        }

        composable<ExerciseDetailRoute>{
            ExerciseDetailScreenRoot(
                onBack = {
                    navController.navigateUp()
                },
                onFinish = {
                    navController.navigateUp()
                }
            )
        }

    }
}