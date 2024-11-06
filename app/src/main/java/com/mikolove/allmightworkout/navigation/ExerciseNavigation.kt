package com.mikolove.allmightworkout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mikolove.exercise.presentation.ExerciseScreenRoot
import kotlinx.serialization.Serializable

@Serializable
object ExercisesRoute

@Serializable
object ExerciseListRoute

fun NavController.navigateToExercise(navOptions: NavOptions) =
    navigate(ExercisesRoute, navOptions)

fun NavGraphBuilder.exercisesGraph(
    navController: NavController,
){
    navigation<ExercisesRoute>(startDestination = ExerciseListRoute){

        composable<ExerciseListRoute>{
            ExerciseScreenRoot()
        }
    }
}