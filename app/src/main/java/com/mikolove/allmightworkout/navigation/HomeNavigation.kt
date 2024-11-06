package com.mikolove.allmightworkout.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mikolove.core.presentation.designsystem.components.AmwActionButton
import kotlinx.serialization.Serializable

@Serializable object AnalyticsRoute

@Serializable object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) =
    navigate(HomeRoute, navOptions)

fun NavGraphBuilder.analyticsGraph(
    navController: NavController,
    onLogout: () -> Unit
){
    navigation<AnalyticsRoute>(startDestination = IntroRoute){

        composable<HomeRoute>{

            Column(
                modifier = Modifier.fillMaxSize()                 ,
                verticalArrangement = Arrangement.Center
            ) {
                Text("THIS IS HOME")
                AmwActionButton(
                    text = "Logout",
                    isLoading = false,
                    enabled = true,
                    onClick = {
                        onLogout()
                    }
                )
            }

        }
    }
}