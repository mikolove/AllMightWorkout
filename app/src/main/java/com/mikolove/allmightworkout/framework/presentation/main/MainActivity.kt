package com.mikolove.allmightworkout.framework.presentation.main

import TopBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.work.WorkInfo
import androidx.work.WorkManager

import com.example.compose.AllMightTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.UIController
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.main.compose.ui.BaseScaffold
import com.mikolove.allmightworkout.framework.presentation.main.compose.ui.BottomBar
import com.mikolove.allmightworkout.framework.presentation.main.compose.ui.QueueProcessing

import com.mikolove.allmightworkout.framework.presentation.network.NetworkEvents
import com.mikolove.allmightworkout.framework.presentation.network.NetworkManager
import com.mikolove.allmightworkout.framework.presentation.session.GoogleAuthUiClient
import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents
import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents.*
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.mikolove.allmightworkout.framework.presentation.session.SessionState
import com.mikolove.allmightworkout.util.printLogD
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.LoadingScreenDestination
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity(), UIController{

    private val TAG: String = "AppDebug"

    @Inject
    lateinit var networkManager: NetworkManager

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var workManager: WorkManager

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            serverClientId = applicationContext.getString(R.string.web_client_id),
            oneTapClient = Identity.getSignInClient(applicationContext),
            auth = Firebase.auth
        )
    }

    //private var dialogInView: MaterialDialog? = null
    private var mainFabController: FabController? = null

    private val DestinationSpec.shouldShowTopBar : Boolean
        get() = when(this){
            is LoadingScreenDestination -> false
            else -> true
        }

    private val DestinationSpec.shouldShowBottomBar : Boolean
        get() = when(this){
            is LoadingScreenDestination -> false
            else -> true
        }

    private val DestinationSpec.shouldShowFloatingButton : Boolean
        get() = when(this){
            is LoadingScreenDestination -> false
            else -> true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            AllMightTheme {

                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()
                val startRoute = NavGraphs.root.startRoute

                val snackbarHostState = remember { SnackbarHostState() }
                val googleAuthUiClient = remember{ googleAuthUiClient }

                val sessionManagerState by sessionManager.state.collectAsStateWithLifecycle()
                val networkManagerState by networkManager.state.collectAsStateWithLifecycle()


                sessionManagerState.syncUUID?.let {
                    workManager.getWorkInfoByIdFlow(it)
                        .onEach {workInfo ->
                            if (workInfo.state == WorkInfo.State.SUCCEEDED || workInfo.state == WorkInfo.State.FAILED)
                                sessionManager.onTriggerEvent(SyncResult(workInfo))
                        }
                        .launchIn(lifecycleScope)
                }

                printLogD("MainActivity", "SessionManager State $sessionManagerState")
                printLogD("MainActivity", "NetworkManager State $networkManagerState")

                BaseScaffold(
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

                    diconnectedGoToLoading(sessionManagerState,navController)

                }
            }
        }

        /*        binding = ActivityMainBinding.inflate(layoutInflater)
         val view = binding.root
         setContentView(view)

         setSupportActionBar(binding.materialToolBar)
         supportActionBar?.setDisplayShowTitleEnabled(true)

         setupProgressLinearIndicator()

         val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
         val navController = navHostFragment.navController

         appBarConfiguration = AppBarConfiguration.Builder(
             R.id.loading_fragment,
             R.id.history_fragment,
             R.id.workout_fragment,
             R.id.exercise_fragment
         ).build()

         setupActionBarWithNavController(
             navController,
             appBarConfiguration)

         bottomNavBar = binding.mainBottomNavigation
         bottomNavBar.setOnNavigationItemSelectedListener { item ->

             when(item.itemId){
                 R.id.page_1 -> {
                     lifecycleScope.launch{
                         sessionManager.onTriggerEvent(SessionEvents.Signout)
                     }
                     //navigateToHistory()
                     true
                 }
                 R.id.page_2 -> {
                     navigateToChooseWorkout()
                     true
                 }
                 R.id.page_3 -> {
                     navigateToChooseExercise()
                     true
                 }
                 else -> false
             }
         }
         binding.floatingActionButton.setOnClickListener {
             mainFabController?.fabOnClick()
         }

         subscribeObserver()*/
    }

    @Composable
    private fun diconnectedGoToLoading(
        sessionState : SessionState,
        navController: NavController
    ){
        val currentDestination by navController.currentDestinationAsState()

        if(!sessionState.firstLaunch && sessionState.user == null && currentDestination != LoadingScreenDestination){
            navController.navigate(LoadingScreenDestination) {
                launchSingleTop = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
//Launch sessionManager checkAuth
    }

    override fun onResume() {
        super.onResume()
        //sessionManager.onTriggerEvent(GetAuthState)
    }

    fun subscribeObserver(){

        /*sessionManager.state.observe(this) { state ->

        displayProgressBar(state.isLoading)

        processQueue(
         context = this,
         queue = state.queue,
         stateMessageCallback = object : StateMessageCallback {
             override fun removeMessageFromQueue() {
                 sessionManager.onTriggerEvent(SessionEvents.OnRemoveHeadFromQueue)
             }
         })

        printLogD("MainActivity","Session manager observer ${sessionManager.isAuth()}")

        if(!state.firstLaunch && state.user == null){
         navigateToLoading()
        }
        }*/
    }

    override fun loadFabController(fabController: FabController?) {
//LETS GO JETPACK COMPOSE
        /*printLogD("MainActivity","LoadFabController ${fabController}")
        mainFabController = fabController
        mainFabController?.let {
         binding.floatingActionButton.show()
        }?: binding.floatingActionButton.hide()*/
    }

    /*   private fun navigateToLoading(){
           findNavController(R.id.main_fragment_container)
               .navigate(R.id.action_global_loading_fragment)
       }

       private fun navigateToHistory(){
           findNavController(R.id.main_fragment_container)
               .navigate(R.id.action_global_history_fragment)
       }

       private fun navigateToChooseExercise(){
           findNavController(R.id.main_fragment_container)
               .navigate(R.id.action_global_exercise_fragment)
       }

       private fun navigateToChooseWorkout(){
           findNavController(R.id.main_fragment_container)
               .navigate(R.id.action_global_workout_fragment)
       }
   */
    /*
    //LETS GO COMPOSE
    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.main_fragment_container)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/

    override fun displayAppBar() {
//LETS GO JETPACK COMPOSE
        /*        val navController = findNavController(R.id.main_fragment_container)
        val visibility = when(navController.currentDestination?.id) {
         R.id.loading_fragment -> View.GONE
         else -> View.VISIBLE
        }

        if(binding.appBarLayout.visibility != visibility)
         binding.appBarLayout.visibility = visibility*/
    }

    override fun displayBottomNavigation() {
//LETS GO JETPACK COMPOSE
        /*        val container = binding.mainActivityContainer
         val slide = Slide()
         slide.duration = 200
         slide.addTarget(bottomNavBar)
         TransitionManager.beginDelayedTransition(container,slide)

         val navController = findNavController(R.id.main_fragment_container)
         val visibility = when(navController.currentDestination?.id) {

             R.id.loading_fragment -> View.GONE
             R.id.history_fragment -> View.VISIBLE
             R.id.workout_fragment -> View.VISIBLE
             R.id.exercise_fragment -> View.VISIBLE
             R.id.workout_detail_fragment -> View.GONE
             R.id.exercise_detail_fragment -> View.GONE
             R.id.exercise_set_detail_fragment -> View.GONE
             R.id.add_exercise_to_workout_fragment -> View.GONE
             R.id.workoutInProgressFragment -> View.GONE
             R.id.exerciseInProgressFragment -> View.GONE
             else -> View.VISIBLE
         }

         if(bottomNavBar.visibility != visibility)
             bottomNavBar.visibility = visibility*/
    }

    private fun setupProgressLinearIndicator(){
//LETS GO JETPACK COMPOSE
        /*  binding.mainProgressBar.hide()
        binding.mainProgressBar.setVisibilityAfterHide(View.INVISIBLE)*/
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        /*        if(isDisplayed) {
             //binding.mainProgressBar.visible()
             binding.mainProgressBar.show()
         }else {
             //binding.mainProgressBar.invisible()
             binding.mainProgressBar.hide()
         }*/
    }

    override fun displayAppBarTitle() {

        /*        val navController = findNavController(R.id.main_fragment_container)
         printLogD("MainActivity","Current destination ${navController.currentDestination?.id}")
         val appTitle = when(navController.currentDestination?.id) {

             R.id.history_fragment -> R.string.fragment_home_tab_layout_history
             R.id.workout_fragment -> R.string.fragment_home_tab_layout_workout
             R.id.exercise_fragment -> R.string.fragment_home_tab_layout_exercise
             R.id.exercise_detail_fragment -> R.string.fragment_exercise_detail_title
             R.id.exercise_set_detail_fragment -> R.string.fragment_exercise_set_detail_title
             R.id.workout_detail_fragment -> R.string.fragment_manage_workout_text_title
             R.id.workoutInProgressFragment -> R.string.wip_fragment_title
             R.id.exerciseInProgressFragment -> R.string.eip_fragment_title
             else -> R.string.app_bar_title_default
         }
         supportActionBar?.setDisplayShowTitleEnabled(true)
         supportActionBar?.setTitle(appTitle)*/
    }


    override fun hideSoftKeyboard() {
        /* if (currentFocus != null) {
        val inputMethodManager = getSystemService(
          Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager
          .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }*/
    }

    override fun onPause() {
        super.onPause()
        /*        if(dialogInView != null){
             (dialogInView as MaterialDialog).dismiss()
             dialogInView = null
         }*/
    }

}

