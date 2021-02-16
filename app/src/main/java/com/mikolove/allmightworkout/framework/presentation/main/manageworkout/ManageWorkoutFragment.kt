package com.mikolove.allmightworkout.framework.presentation.main.manageworkout

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.RemoveWorkout.Companion.DELETE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.UpdateWorkout.Companion.UPDATE_WORKOUT_FAILED
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.UpdateWorkout.Companion.UPDATE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentManageWorkoutBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.WorkoutInteractionState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

const val MANAGE_WORKOUT_STATE_BUNDLE_KEY = "com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state"

@AndroidEntryPoint
class ManageWorkoutFragment(): BaseFragment(R.layout.fragment_manage_workout){

    private var binding : FragmentManageWorkoutBinding? = null
    val viewModel : ManageWorkoutViewModel by activityViewModels()

    /********************************************************************
        MENU INTERACTIONS
    *********************************************************************/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_manage_workout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_manage_workout_validate -> {
                insertWorkout()
                true
            }
            R.id.toolbar_manage_workout_delete -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
            Load Fragment
         */
        setHasOptionsMenu(true)
        binding = FragmentManageWorkoutBinding.bind(view)

        setupUI()
        setupOnBackPressDispatcher()

        subscribeObservers()
        getWorkoutFromPreviousFragment()

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


    private fun setupUI(){
        binding?.fragmentManageWorkoutEditName?.disableContentInteraction()
    }



    /********************************************************************
        OBSERVERS
    *********************************************************************/

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){

                viewState.workout?.let { workout ->
                    printLogD("ManageWorkoutFragment","Load Workout UI")
                    setWorkoutUi(workout)
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
                        viewModel.clearStateMessage()
                    }

                    DELETE_WORKOUT_SUCCESS -> {
                        viewModel.clearStateMessage()
                        findNavController().popBackStack()
                    }

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

                            MANAGE_WORKOUT_ERRROR_RETRIEVING_ID_WORKOUT -> {
                                findNavController().popBackStack()
                            }

                            else -> {
                                // do nothing
                            }
                        }
                    }
                }
            }

        })

        viewModel.workoutNameInteractionState.observe(viewLifecycleOwner, Observer { state ->

            when(state){

                is WorkoutInteractionState.EditState -> {
                    printLogD("ManageWorkoutFragment","Enter editing state")
                    binding?.fragmentManageWorkoutEditName?.enableContentInteraction()
                    view?.showKeyboard()
                    viewModel.setIsUpdatePending(true)
                }

                is WorkoutInteractionState.DefaultState -> {
                    printLogD("ManageWorkoutFragment","Enter default state")
                    binding?.fragmentManageWorkoutEditName?.disableContentInteraction()
                }
            }
        })

    }

    /********************************************************************
        GETS ARGS BUNDLE
     *********************************************************************/

    private fun getWorkoutFromPreviousFragment(){
        printLogD("ManageWorkoutFragment","Load workout")

        arguments?.let { args ->
            (args.getString(MANAGE_WORKOUT_ID_WORKOUT_BUNDLE_KEY))?.let { idWorkout ->
                printLogD("ManageWorkoutFragment","Args found load workout")
                printLogD("ManageWorkoutFragment","id workout loaded is ${idWorkout}")
                viewModel.setStateEvent(ManageWorkoutStateEvent.GetWorkoutByIdEvent(idWorkout))
            }
        } ?: loadDefaultWorkout()

    }

    private fun loadDefaultWorkout(){
        printLogD("ManageWorkoutFragment","No workout found load default")
        val workout = viewModel.createWorkout()
        viewModel.setIsNewWorkout(true)
        viewModel.setWorkout(workout)
    }

    private fun onErrorRetrievingNoteFromPreviousFragment(){
        viewModel.setStateEvent(
            ManageWorkoutStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = MANAGE_WORKOUT_ERRROR_RETRIEVING_ID_WORKOUT,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    )
                )
            )
        )
    }


    private fun insertWorkout(){
        if(viewModel.getIsNewWorkout()){
            if(viewModel.checkEditState()){
                quitEditState()
            }

            viewModel.setStateEvent(ManageWorkoutStateEvent.InsertWorkoutEvent())

        }
    }
    private fun updateWorkout() {
        if(!viewModel.getIsNewWorkout()){
            if(viewModel.getIsUpdatePending()){
                viewModel.setStateEvent(
                    ManageWorkoutStateEvent.UpdateWorkoutEvent()
                )
            }
        }
    }

    /********************************************************************
        SETTERS - GETTERS - ACTIONS
    *********************************************************************/

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
        printLogD("ManageWorkoutFragment","Name in edit mode")
        if(!viewModel.isEditingName()){
            updateNameInViewModel()
            viewModel.setWorkoutInteractionNameState(WorkoutInteractionState.EditState())
        }
    }

    private fun updateNameInViewModel(){
        if(viewModel.isEditingName()){
            viewModel.updateWorkoutName(getWorkoutName())
        }
    }

    private fun getWorkoutName() : String{
        return binding?.fragmentManageWorkoutEditName?.text.toString()
    }

    private fun onClickWorkoutIsActive(){
        printLogD("ManageWorkoutFragment","Is Active clicked")
        updateIsActiveInViewModel()
    }

    private fun updateIsActiveInViewModel(){
        viewModel.updateWorkoutIsActive(getWorkoutIsActive())
    }

    private fun getWorkoutIsActive() : Boolean{
        return binding?.fragmentManageWorkoutSwitchIsActive?.let {
            it.isChecked
        }?: false

    }


    private fun quitEditState(){
        updateNameInViewModel()
        //updateIsActiveInViewModel()
        viewModel.exitEditState()
        view?.hideKeyboard()
    }



    /********************************************************************
        BACK BUTTON PRESS
    *********************************************************************/
    private fun onBackPressed() {
        printLogD("ManageWorkoutFragment","OnBackPressed")
        if (viewModel.checkEditState()) {
            quitEditState()
         }else{
            findNavController().popBackStack()
        }
    }

    private fun setupOnBackPressDispatcher() {
        printLogD("ManageWorkoutFragment","Setup Back dispatcher")
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


}