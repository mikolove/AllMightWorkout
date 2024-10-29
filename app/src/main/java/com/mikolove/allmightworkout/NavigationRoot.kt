package com.mikolove.allmightworkout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mikolove.auth.presentation.intro.IntroScreenRoot
import com.mikolove.auth.presentation.register.RegisterScreenRoot
import com.mikolove.core.presentation.designsystem.components.AmwActionButton

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "auth"
    ) {
        authGraph(navController)
        homeGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = "intro",
        route = "auth"
    ) {
        composable(route = "intro") {
            IntroScreenRoot(
                onSignUpClick = {
                    navController.navigate("signup")
                },
                onSignInClick = {
                    navController.navigate("signin")
                }
            )
        }
        composable(route = "signup") {
            RegisterScreenRoot(
                {},
                {}
           /*     onSignInClick = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate("login")
                }*/
            )
        }
    }
}

private fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(
        startDestination = "welcome",
        route ="home"
    ){
        composable(route = "welcome") {
            Text("THIS IS HOME")
        }
    }
}