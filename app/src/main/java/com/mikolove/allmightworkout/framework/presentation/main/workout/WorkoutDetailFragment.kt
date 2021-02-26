package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveWorkout
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveWorkout.Companion.DELETE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.workout.UpdateWorkout.Companion.UPDATE_WORKOUT_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workout.UpdateWorkout.Companion.UPDATE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentWorkoutDetailBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutInteractionState.*
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutStateEvent.*
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailFragment: BaseFragment(R.layout.fragment_workout_detail){

    private var binding : FragmentWorkoutDetailBinding? = null
    val viewModel : WorkoutViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.main_fragment_container
            duration = 300.toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.backgroundColor))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
            Load Fragment
         */
        setHasOptionsMenu(true)
        binding = FragmentWorkoutDetailBinding.bind(view)

        setupUI()
        setupOnBackPressDispatcher()
        subscribeObservers()
        setupBottomNav()
        //getWorkoutFromPreviousFragment()

        /*
            Bind listener
         */
        binding?.fragmentManageWorkoutEditName?.setOnClickListener{
            onClickWorkoutEditName()
        }

        binding?.fragmentManageWorkoutSwitchIsActive?.setOnClickListener {
            onClickWorkoutIsActive()
        }
    }


    private fun setupBottomNav(){
        uiController?.displayBottomNavigation(View.GONE)
    }

    override fun onDestroyView() {

        /*
            Unbind view state
         */
        if (viewModel.checkEditState()) {
            viewModel.exitEditState()
        }
        viewModel.setWorkoutSelected(null)
        viewModel.setIsUpdatePending(false)

        super.onDestroyView()
    }

    /********************************************************************
    MENU INTERACTIONS
     *********************************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workout_detail, menu)

        val menuValidate = menu.findItem(R.id.toolbar_manage_workout_validate)
        setMenuVisibility(menuValidate,viewModel.getIsUpdatePending())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_manage_workout_validate -> {
                updateWorkout()
                true
            }
            R.id.toolbar_manage_workout_delete -> {
                deleteWorkout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setMenuVisibility(menuItem: MenuItem, isVisible : Boolean){
        menuItem.setVisible(isVisible)
    }

    /********************************************************************
        SETUP
    *********************************************************************/

    private fun setupUI(){
        binding?.fragmentManageWorkoutEditName?.disableContentInteraction()
    }

    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    /********************************************************************
        OBSERVERS
    *********************************************************************/

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){

                viewState.workoutSelected?.let { workout ->
                    printLogD("WorkoutDetailFragment","Workout selected load UI")
                    setWorkoutUi(workout)
                }

                viewState.isUpdatePending?.let { isUpdatePending ->
                    activity?.invalidateOptionsMenu()
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when(response.message){

                    UPDATE_WORKOUT_SUCCESS -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                        viewModel.setIsUpdatePending(false)
                    }

                    DELETE_WORKOUT_SUCCESS -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                        findNavController().popBackStack()
                    }

                    //If another we quit so we clear Message Stack
                    else -> {

                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                        when(response.message){

                            UPDATE_WORKOUT_FAILED -> {
                                findNavController().popBackStack()
                            }

                            WORKOUT_ERRROR_RETRIEVING_ID_WORKOUT -> {
                                findNavController().popBackStack()
                            }

                            else -> { }
                        }
                    }
                }
            }

        })

        viewModel.workoutNameInteractionState.observe(viewLifecycleOwner, Observer { state ->

            when(state){

                is EditState -> {
                    binding?.fragmentManageWorkoutEditName?.enableContentInteraction()
                    view?.showKeyboard()
                    viewModel.setIsUpdatePending(true)
                }

                is DefaultState -> {
                    binding?.fragmentManageWorkoutEditName?.disableContentInteraction()
                    view?.hideKeyboard()
                }
            }
        })

        viewModel.workoutIsActiveInteractionState.observe(viewLifecycleOwner, Observer { state ->

            when(state){

                is EditState -> {
                    viewModel.setIsUpdatePending(true)
                }
                is DefaultState -> { }
            }
        })

    }

    /********************************************************************
        GETS ARGS BUNDLE
     *********************************************************************/

    private fun getWorkoutFromPreviousFragment(){
        arguments?.let { args ->
            (args.getString(WORKOUT_ID_WORKOUT_BUNDLE_KEY))?.let { idWorkout ->
                viewModel.setStateEvent(GetWorkoutByIdEvent(idWorkout))
            }
        } ?: onErrorRetrievingWorkoutFromPreviousFragment()

    }

    private fun onErrorRetrievingWorkoutFromPreviousFragment(){
        viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = WORKOUT_ERRROR_RETRIEVING_ID_WORKOUT,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        )
    }


    /********************************************************************
        SETTERS - GETTERS - ACTIONS
    *********************************************************************/

    private fun deleteWorkout() {
        viewModel.setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = RemoveWorkout.DELETE_WORKOUT_ARE_YOU_SURE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    viewModel.setStateEvent(WorkoutStateEvent.RemoveWorkoutEvent())
                                }
                                override fun cancel() {}
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )
    }

    private fun updateWorkout() {
        if(viewModel.getIsUpdatePending()){
            if (viewModel.checkEditState()) {
                quitEditState()
            }
            viewModel.updateWorkout()
        }
    }

    private fun quitEditState(){
        updateNameInViewModel()
        updateIsActiveInViewModel()
        viewModel.exitEditState()
    }

    private fun setWorkoutUi(workout : Workout){
        setWorkoutName(workout.name)
        setWorkoutIsActive(workout.isActive)
    }

    private fun setWorkoutName(name : String){
        binding?.fragmentManageWorkoutEditName?.setText(name)
    }

    private fun setWorkoutIsActive(isActive : Boolean){
        binding?.fragmentManageWorkoutSwitchIsActive?.isChecked = isActive
    }

    private fun onClickWorkoutEditName(){
        if(!viewModel.isEditingName()){
            updateIsActiveInViewModel()
            viewModel.setWorkoutInteractionNameState(EditState())
        }
    }

    private fun onClickWorkoutIsActive(){
        if(!viewModel.isEditingIsActive()){
            updateNameInViewModel()
            viewModel.setWorkoutInteractionIsActiveState(EditState())
            //Change state on first click
            setWorkoutIsActive(!getWorkoutIsActive())
        }
    }

    private fun updateNameInViewModel(){
        if(viewModel.isEditingName()){
            viewModel.updateWorkoutName(getWorkoutName())
        }
    }
    private fun updateIsActiveInViewModel(){
        if(viewModel.isEditingIsActive()){
            viewModel.updateWorkoutIsActive(getWorkoutIsActive())
        }
    }

    private fun getWorkoutName() : String{
        return binding?.fragmentManageWorkoutEditName?.text.toString()
    }

    private fun getWorkoutIsActive() : Boolean{
        return binding?.fragmentManageWorkoutSwitchIsActive?.let {
            it.isChecked
        }?: false
    }

    /********************************************************************
        BACK BUTTON PRESS
    *********************************************************************/

    private fun onBackPressed() {
        printLogD("ManageWorkoutFragment","ON BACK PRESSED")
        if (viewModel.checkEditState()) {
            quitEditState()
         }else{

            findNavController().popBackStack()
        }
    }
}