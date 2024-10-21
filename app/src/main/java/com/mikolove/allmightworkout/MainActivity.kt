package com.mikolove.allmightworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents.*
import com.mikolove.allmightworkout.util.GoogleAuthUiClient
import com.mikolove.core.presentation.designsystem.AmwTheme
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity(){

    private val TAG: String = "AppDebug"

    //@Inject
    //lateinit var networkManager: NetworkManager

    //@Inject
    //lateinit var sessionManager: SessionManager

    @Inject
    lateinit var workManager: WorkManager

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            serverClientId = applicationContext.getString(R.string.web_client_id),
            oneTapClient = Identity.getSignInClient(applicationContext),
            auth = Firebase.auth
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            AmwTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val googleAuthUiClient = remember{ googleAuthUiClient }
                    val snackbarHostState = remember { SnackbarHostState() }

                    //val sessionManagerState by sessionManager.state.collectAsStateWithLifecycle()
                    //val networkManagerState by networkManager.state.collectAsStateWithLifecycle()

                    Text("Hello World!")
                }


                /*sessionManagerState.syncUUID?.let {
                    workManager.getWorkInfoByIdFlow(it)
                        .onEach {workInfo ->
                            if (workInfo.state == WorkInfo.State.SUCCEEDED || workInfo.state == WorkInfo.State.FAILED)
                                sessionManager.onTriggerEvent(SyncResult(workInfo))
                        }
                        .launchIn(lifecycleScope)
                }

                printLogD("MainActivity", "SessionManager State $sessionManagerState")
                printLogD("MainActivity", "NetworkManager State $networkManagerState")
                */

                /*BaseScaffold(
                    startRoute = startRoute,
                    navController = navController,
                    topBar = { dest, backStackEntry ->
                        if (dest.shouldShowTopBar) {
                            TopBar(dest, backStackEntry)
                        }
                    },
                    bottomBar = {
                        if (it.shouldShowBottomBar) {
                            BottomBar(navController)
                        }
                    },
                    floatingActionButton = {
                        if(it.shouldShowFloatingButton){
                            ExtendedFloatingActionButton(
                                text = { Text("Show snackbar") },
                                icon = { /*Icon(Icons.Filled.Image, contentDescription = "") */},
                                onClick = {}
                            )
                        }
                    },
                    snackbarHostState = snackbarHostState) {

                    QueueProcessing(
                        name ="SessionManager",
                        queue = sessionManagerState.queue,
                        onRemoveHeadFromQueue = { sessionManager.onTriggerEvent(OnRemoveHeadFromQueue) },
                        snackbarHostState = snackbarHostState,
                        progressBarState = sessionManagerState.isLoading,) { }

                   QueueProcessing(
                        name ="NetworkManager",
                        queue = networkManagerState.queue,
                        onRemoveHeadFromQueue = { networkManager.onTriggerEvent(NetworkEvents.OnRemoveHeadFromQueue) },
                        snackbarHostState = snackbarHostState,
                        progressBarState = false,) { }

                    DestinationsNavHost(
                        dependenciesContainerBuilder = { //this: DependenciesContainerBuilder<*>
                            dependency(snackbarHostState)
                            dependency(googleAuthUiClient)
                        },
                        engine = engine,
                        navController = navController,
                        navGraph = NavGraphs.root,
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        startRoute = startRoute
                    )
                }*/
            }
        }
    }

}

