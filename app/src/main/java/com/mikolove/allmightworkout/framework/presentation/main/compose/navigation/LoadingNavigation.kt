/*
package com.mikolove.allmightworkout.framework.presentation.main.compose.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mikolove.allmightworkout.framework.presentation.main.compose.navigation.AppNavigation.Companion.loadingRoute
import com.mikolove.allmightworkout.framework.presentation.main.compose.ui.LoadingScreen
import com.mikolove.allmightworkout.framework.presentation.main.loading.LoadingEvents
import com.mikolove.allmightworkout.framework.presentation.main.loading.LoadingStep
import com.mikolove.allmightworkout.framework.presentation.main.loading.LoadingViewModel
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.launch

object Loading : AppNavigation{

    override val route: String = loadingRoute

    override val screen: @Composable (navController: NavHostController, arguments: Bundle?) -> Unit = { navController,  _ ->

        val scope = rememberCoroutineScope()
        val viewModel : LoadingViewModel = hiltViewModel()
        val sessionManager = viewModel.sessionManager

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if(result.resultCode == ComponentActivity.RESULT_OK) {
                    scope.launch {
                        val signInResult = sessionManager.googleAuthUiClient.signInWithIntent(intent = result.data ?: return@launch)
                        viewModel.onTriggerEvent(LoadingEvents.SignInResult(signInResult))
                    }
                }else{
                    viewModel.onTriggerEvent(LoadingEvents.LoadStep(LoadingStep.INIT))
                }
            }
        )

        LoadingScreen(
            viewModel = viewModel ,
            onSignInClick = {
                scope.launch {
                    printLogD("MainActivity","Clicked")
                    val signInIntentSender = sessionManager.googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            },
            onNavigateTo = {navController.navigate("")}
        )
    }
}*/
