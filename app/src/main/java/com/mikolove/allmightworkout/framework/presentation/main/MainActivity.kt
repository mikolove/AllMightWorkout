package com.mikolove.allmightworkout.framework.presentation.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.state.UIComponentType.*
import com.mikolove.allmightworkout.databinding.ActivityMainBinding
import com.mikolove.allmightworkout.framework.presentation.UIController
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.processQueue
import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents
import com.mikolove.allmightworkout.framework.presentation.session.SessionLoggedType
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    UIController{

    private val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavBar : BottomNavigationView
    private lateinit var binding : ActivityMainBinding
    private var dialogInView: MaterialDialog? = null
    private var mainFabController: FabController? = null

    //FIX leak android Q maybe
    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        if(isTaskRoot && navHostFragment.childFragmentManager.backStackEntryCount == 0) {
            finishAfterTransition()
        }else {
            super.onBackPressed()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
                    navigateToHistory()
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

        subscribeObserver()
    }

    override fun onStart() {
        super.onStart()
        //Launch sessionManager checkAuth
    }

    fun subscribeObserver(){
        sessionManager.state.observe(this) { state ->

            displayProgressBar(state.isLoading)

            processQueue(
                context = this,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        sessionManager.onTriggerEvent(SessionEvents.OnRemoveHeadFromQueue)
                    }
                })

/*            if (state.idUser == null && state.checkAuth) {
                state.logged is SessionLoggedType.DISCONNECTED
                onTriggerEvent(Logout())
                navigateToLoading()
            }

            if( state.logged == SessionLoggedType.DISCONNECTED){
                sessionManager.onTriggerEvent(SessionEvents.Logout)
            }*/

        }
    }

    override fun loadFabController(fabController: FabController?) {
        printLogD("MainActivity","LoadFabController ${fabController}")
        mainFabController = fabController
        mainFabController?.let {
            binding.floatingActionButton.show()
        }?: binding.floatingActionButton.hide()
    }

    private fun navigateToLoading(){
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_fragment_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun displayAppBar() {
        val navController = findNavController(R.id.main_fragment_container)
        val visibility = when(navController.currentDestination?.id) {
            R.id.loading_fragment -> View.GONE
            else -> View.VISIBLE
        }

        if(binding.appBarLayout.visibility != visibility)
            binding.appBarLayout.visibility = visibility
    }

    override fun displayBottomNavigation() {

        val container = binding.mainActivityContainer
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
            bottomNavBar.visibility = visibility
    }

    private fun setupProgressLinearIndicator(){
        binding.mainProgressBar.hide()
        binding.mainProgressBar.setVisibilityAfterHide(View.INVISIBLE)
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        if(isDisplayed) {
            //binding.mainProgressBar.visible()
            binding.mainProgressBar.show()
        }else {
            //binding.mainProgressBar.invisible()
            binding.mainProgressBar.hide()
        }
    }

    override fun displayAppBarTitle() {

        val navController = findNavController(R.id.main_fragment_container)
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
        supportActionBar?.setTitle(appTitle)
    }


    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        if(dialogInView != null){
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }


}