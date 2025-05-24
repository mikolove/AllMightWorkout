package com.mikolove.allmightworkout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mikolove.allmightworkout.navigation.AuthRoute
import com.mikolove.allmightworkout.presentation.AmwAppRoot
import com.mikolove.allmightworkout.presentation.rememberAmwAppState
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.components.Background
import com.mikolove.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity(){

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isLoadingData
            }
        }

        enableEdgeToEdge()

        setContent {

            val appState = rememberAmwAppState()

            AmwTheme {

                KoinAndroidContext{

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
                                    if ( !viewModel.state.isLoadingData && viewModel.state.isWorkoutTypesChecked) {
                                        appState.navController.navigate(AuthRoute)
                                    }
                                }
                            }
                        }

                        if (!viewModel.state.isLoadingData &&
                            viewModel.state.isWorkoutTypesChecked) {

                            AmwAppRoot(
                                appState = appState,
                                modifier = Modifier,
                                isLoggedIn = viewModel.state.isLoggedIn,
                                onLogout = { viewModel.logout() }
                            )

                        }else{
                            ReloadScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReloadScreen(){
    Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.first_loading_error_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.first_loading_error_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
@Preview
@Composable
private fun ReloadScreenPreview(){
    AmwTheme {
        ReloadScreen()
    }
}
