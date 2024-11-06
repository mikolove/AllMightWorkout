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
import com.mikolove.allmightworkout.navigation.AuthRoute
import com.mikolove.allmightworkout.presentation.AmwAppRoot
import com.mikolove.allmightworkout.presentation.rememberAmwAppState
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity(){

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth && viewModel.state.isLoadingData
            }
        }

        enableEdgeToEdge()

        setContent {

            val appState = rememberAmwAppState()

            AmwTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val context = LocalContext.current

                    ObserveAsEvents(viewModel.events) { event ->
                        when (event) {
                            is MainEvent.Error -> {
                                Toast.makeText(
                                    context,
                                    event.error.asString(context),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            MainEvent.Logout -> {
                                appState.navController.navigate(AuthRoute)
                            }

                        }
                    }

                    if (!viewModel.state.isCheckingAuth) {
                        AmwAppRoot(
                            appState = appState,
                            modifier = Modifier,
                            isLoggedIn = viewModel.state.isLoggedIn,
                            onLogout = { viewModel.logout() }
                        )
                    }
                }
            }
        }
    }
}

