package com.mikolove.allmightworkout.framework.presentation.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.state.UIComponentType.*
import com.mikolove.allmightworkout.databinding.ActivityMainBinding
import com.mikolove.allmightworkout.framework.presentation.UIController
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.displayToast
import com.mikolove.allmightworkout.util.TodoCallback
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    UIController{

    private val TAG: String = "AppDebug"

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

    }


    override fun loadFabController(fabController: FabController?) {
        printLogD("MainActivity","LoadFabController ${fabController}")
        mainFabController = fabController
        mainFabController?.let {
            binding.floatingActionButton.show()
        }?: binding.floatingActionButton.hide()
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
        val visibility = when(navController?.currentDestination?.id) {
            R.id.loading_fragment -> View.GONE
            else -> View.VISIBLE
        }

        if(binding?.appBarLayout.visibility != visibility)
            binding?.appBarLayout.visibility = visibility
    }

    override fun displayBottomNavigation() {

        val container = binding?.mainActivityContainer
        val slide = Slide()
        slide.duration = 200
        slide.addTarget(bottomNavBar)
        TransitionManager.beginDelayedTransition(container,slide)

        val navController = findNavController(R.id.main_fragment_container)
        val visibility = when(navController?.currentDestination?.id) {

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

/*    override fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback) {
        dialogInView = MaterialDialog(this).show {
            title(text = title)
            input(
                waitForPositiveButton = true,
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            ){ _, text ->
                callback.onTextCaptured(text.toString())
            }
            positiveButton(R.string.text_ok)
            onDismiss {
                dialogInView = null
            }
            cancelable(true)
        }
    }*/

    override fun displayAppBarTitle() {

        val navController = findNavController(R.id.main_fragment_container)
        printLogD("MainActivity","Current destination ${navController?.currentDestination?.id}")
        val appTitle = when(navController?.currentDestination?.id) {

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
/*

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        when(response.uiComponentType){

            is SnackBar -> {
                val onDismissCallback: TodoCallback?
                        = response.uiComponentType.onDismissCallback
                val undoCallback: SnackbarUndoCallback?
                        = response.uiComponentType.undoCallback
                response.message?.let { msg ->
                    displaySnackbar(
                        message = msg,
                        snackbarUndoCallback = undoCallback,
                        onDismissCallback = onDismissCallback,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is SimpleSnackBar -> {
                response.message?.let { msg ->
                    displaySimpleSnackbar(
                        message = msg,
                        stateMessageCallback = stateMessageCallback
                    )
                }

            }

            is AreYouSureDialog -> {

                response.message?.let {
                    dialogInView = areYouSureDialog(
                        message = it,
                        callback = response.uiComponentType.callback,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is AreYouSureSaveDialog -> {
                response.message?.let {
                    dialogInView = areYouSureSaveDialog(
                        message = it,
                        callback = response.uiComponentType.callback,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is None -> {
                // This would be a good place to send to your Error Reporting
                // software of choice (ex: Firebase crash reporting)
                Log.i(TAG, "onResponseReceived: ${response.message}")
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }


    private fun displaySnackbar(
        message: String,
        snackbarUndoCallback: SnackbarUndoCallback?,
        onDismissCallback: TodoCallback?,
        stateMessageCallback: StateMessageCallback
    ){
        val snackbar = Snackbar.make(
            binding.mainFragmentContainer,
            message,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(
            getString(R.string.text_undo),
            SnackbarUndoListener(snackbarUndoCallback)
        )
        snackbar.addCallback(object: BaseTransientBottomBar.BaseCallback<Snackbar>(){
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                onDismissCallback?.execute()
            }
        })
        snackBarAnchorView(snackbar)
        snackbar.show()
        stateMessageCallback.removeMessageFromStack()
    }

    private fun displaySimpleSnackbar(
        message: String,
        stateMessageCallback: StateMessageCallback
    ){
        val snackbar = Snackbar.make(
            binding.mainFragmentContainer,
            message,
            Snackbar.LENGTH_LONG
        )
        snackBarAnchorView(snackbar)
        snackbar.show()
        stateMessageCallback.removeMessageFromStack()
    }

    private fun snackBarAnchorView(snackbar: Snackbar)
    {
        if(binding.floatingActionButton.isVisible){
            snackbar.setAnchorView(binding.floatingActionButton)
        }else{
            snackbar.setAnchorView(binding.mainBottomNavigation)
        }
    }
    private fun displayDialog(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ){
        response.message?.let { message ->

            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        }?: stateMessageCallback.removeMessageFromStack()
    }
*/

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

 /*   private fun displaySuccessDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_success)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_error)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayInfoDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.text_info)
                message(text = message)
                positiveButton(R.string.text_ok){
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun areYouSureDialog(
        message: String,
        callback: AreYouSureCallback,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.are_you_sure)
                message(text = message)
                negativeButton(R.string.text_cancel){
                    stateMessageCallback.removeMessageFromStack()
                    callback.cancel()
                    dismiss()
                }
                positiveButton(R.string.text_yes){
                    stateMessageCallback.removeMessageFromStack()
                    callback.proceed()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun areYouSureSaveDialog(
        message: String,
        callback: AreYouSureSaveCallback,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show{
                title(R.string.are_you_sure)
                message(text = message)

                negativeButton(R.string.text_note_save){
                    stateMessageCallback.removeMessageFromStack()
                    callback.proceedNotSave()
                    dismiss()
                }

                positiveButton(R.string.text_save){
                    stateMessageCallback.removeMessageFromStack()
                    callback.proceedSave()
                    dismiss()
                }

                onCancel {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }

                onDismiss {
                    dialogInView = null
                }

                cancelable(true)
            }
    }*/

}