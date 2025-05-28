package com.mikolove.exercise.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.mikolove.exercise.presentation.detail.ExerciseDetailScreenRoot
import com.mikolove.exercise.presentation.overview.ExerciseScreenRoot
import com.mikolove.exercise.presentation.search.ExerciseSearchScreenRoot
import kotlinx.serialization.Serializable

@Serializable object ExercisesRoute

@Serializable object ExerciseSearchRoute
@Serializable object ExerciseListRoute
@Serializable data class ExerciseDetailRoute(val id : String)

fun NavController.navigateToExerciseDetail(idExercise : String, navOptions: NavOptionsBuilder.() -> Unit = {}){
    navigate(route = ExerciseDetailRoute(idExercise)){
        navOptions
    }
}

fun NavController.navigateToExercise(navOptions: NavOptions) =
    navigate(ExerciseListRoute, navOptions)

fun NavController.navigateToExerciseSearch(navOptions: NavOptions) =
    navigate(ExerciseSearchRoute,navOptions)

fun NavGraphBuilder.exercisesGraph(
    navController: NavController,
){
    navigation<ExercisesRoute>(startDestination = ExerciseListRoute){

        composable<ExerciseListRoute>{
            ExerciseScreenRoot(
                onSearchClick = {
                    navController.navigateToExerciseSearch(navOptions {})
                },
                onUpsertExerciseClick = {
                    navController.navigateToExerciseDetail(it)
                },
            )
        }

        composable<ExerciseSearchRoute>{
            ExerciseSearchScreenRoot(
                onBack = {navController.navigateUp()},
                onNavigateToExerciseDetail = {
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