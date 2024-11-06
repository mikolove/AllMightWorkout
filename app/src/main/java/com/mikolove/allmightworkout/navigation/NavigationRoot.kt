package com.mikolove.allmightworkout.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.mikolove.auth.presentation.login.LoginScreenRoot
import com.mikolove.auth.presentation.register.RegisterScreenRoot
import com.mikolove.core.presentation.designsystem.components.AmwActionButton

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "auth",

    ) {
        authsGraph(navController)
        analyticsGraph(navController,onLogout)
    }
}

/*
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
                onSuccessfulRegistration = {
                    navController.navigate("signin")
                },
                onSignInClick = {
                    navController.navigate("signin") {
                        popUpTo("signup") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },

            )
        }

        composable(route = "signin"){
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup") {
                        popUpTo("signin") {
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

private fun NavGraphBuilder.homeGraph(navController: NavHostController,    onLogout: () -> Unit
) {
    navigation(
        startDestination = "welcome",
        route ="home"
    ){
        composable(route = "welcome") {
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
}*/
