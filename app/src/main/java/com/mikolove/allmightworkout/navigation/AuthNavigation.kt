package com.mikolove.allmightworkout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mikolove.auth.presentation.intro.IntroScreenRoot
import com.mikolove.auth.presentation.login.LoginScreenRoot
import com.mikolove.auth.presentation.register.RegisterScreenRoot

import kotlinx.serialization.Serializable

@Serializable object AuthRoute

@Serializable object IntroRoute
@Serializable object LoginRoute
@Serializable object RegisterRoute

fun NavController.navigateToAuth(navOptions: NavOptions) =
    navigate(AuthRoute, navOptions)

fun NavGraphBuilder.authsGraph(
    navController: NavHostController,
){
    navigation<AuthRoute>(startDestination = IntroRoute){

        composable<IntroRoute>{
            IntroScreenRoot(
                onSignUpClick = {
                    navController.navigate(RegisterRoute)
                },
                onSignInClick = {
                    navController.navigate(LoginRoute)
                }
            )
        }

        composable<LoginRoute>{
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(HomeRoute) {
                        popUpTo(AuthRoute) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(RegisterRoute) {
                        popUpTo(LoginRoute) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
            )
        }

        composable<RegisterRoute>{
            RegisterScreenRoot(
                onSuccessfulRegistration = {
                    navController.navigate(LoginRoute)
                },
                onSignInClick = {
                    navController.navigate(LoginRoute) {
                        popUpTo(RegisterRoute) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
            )
        }

    }

}