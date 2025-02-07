package com.mikolove.allmightworkout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mikolove.exercise.presentation.detail.ExerciseDetailScreenRoot
import com.mikolove.exercise.presentation.overview.ExerciseScreenRoot
import kotlinx.serialization.Serializable

@Serializable object ExercisesRoute

@Serializable object ExerciseListRoute
@Serializable object ExerciseAddRoute

fun NavController.navigateToExercise(navOptions: NavOptions) =
    navigate(ExerciseListRoute, navOptions)

fun NavGraphBuilder.exercisesGraph(
    navController: NavController,
){
    navigation<ExercisesRoute>(startDestination = ExerciseListRoute){

        composable<ExerciseListRoute>{
            ExerciseScreenRoot(
                onAddExerciseClick = {
                    navController.navigate(ExerciseAddRoute)
                },
                onExerciseClick = {}
            )
        }

        composable<ExerciseAddRoute>{
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