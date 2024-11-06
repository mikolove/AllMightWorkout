package com.mikolove.allmightworkout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.mikolove.allmightworkout.navigation.NavigationRoot
import com.mikolove.allmightworkout.presentation.rememberAmwAppState
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity(){

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply{
            setKeepOnScreenCondition{
                viewModel.state.isCheckingAuth && viewModel.state.isLoadingData
            }
        }

        enableEdgeToEdge()

        setContent{

            val appState = rememberAmwAppState()

            AmwTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val context = LocalContext.current

                    ObserveAsEvents(viewModel.events) { event ->
                        when(event){
                            is MainEvent.Error -> {
                                Toast.makeText(
                                    context,
                                    event.error.asString(context),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            MainEvent.Logout -> {
                                navController.navigate("auth"){
                                    popUpTo("home"){
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }

                    if(!viewModel.state.isCheckingAuth){
                        NavigationRoot(
                            navController = navController,
                            isLoggedIn = viewModel.state.isLoggedIn,
                            onLogout = {viewModel.logout()}
                        )
                    }

                }

            }
        }
    }

}
