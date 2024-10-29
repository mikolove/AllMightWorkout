package com.mikolove.allmightworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.components.AmwActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity(){

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply{
            setKeepOnScreenCondition{
                viewModel.state.isCheckingAuth
            }
        }
        setContent{
            AmwTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(!viewModel.state.isCheckingAuth) {
                        if(viewModel.state.isLoggedIn){

                            AmwActionButton(
                                text = "Logout",
                                isLoading = false,
                                modifier = Modifier.fillMaxSize(),
                                onClick = {
                                    viewModel.logout()
                                }
                            )
                        }else{
                            val navController = rememberNavController()
                            NavigationRoot(
                                navController = navController,
                                isLoggedIn = viewModel.state.isLoggedIn,
                            )

                        }
                    }
                }

            }
        }
    }

}
