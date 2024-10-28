package com.mikolove.allmightworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.mikolove.core.presentation.designsystem.AmwTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mikolove.auth.presentation.register.RegisterScreenRoot
import com.mikolove.core.presentation.designsystem.ContinueWithGoogleIcon

class MainActivity : ComponentActivity(){

    private val TAG: String = "AppDebug"

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

@Preview
@Composable
fun PreviewMain() {
    AmwTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Image(modifier = Modifier.
            fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.android_light_rd_na),
                contentDescription = "Continue with google")
        }

    }
}
