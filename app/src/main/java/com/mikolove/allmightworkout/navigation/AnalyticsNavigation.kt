package com.mikolove.allmightworkout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mikolove.analytics.presentation.HomeScreenRoot
import kotlinx.serialization.Serializable

@Serializable object AnalyticsRoute

@Serializable object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) =
    navigate(AnalyticsRoute, navOptions)

fun NavGraphBuilder.homeGraph(
    navController: NavController,
){
    navigation<HomeRoute>(startDestination = AnalyticsRoute){

        composable<AnalyticsRoute>{
            HomeScreenRoot()
        }
    }
}