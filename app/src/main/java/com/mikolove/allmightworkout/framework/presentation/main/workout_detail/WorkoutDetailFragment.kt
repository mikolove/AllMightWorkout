package com.mikolove.allmightworkout.framework.presentation.main.workout_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.common.GetWorkoutById
import com.mikolove.allmightworkout.business.interactors.main.common.GetWorkoutById.Companion.GET_WORKOUT_BY_ID_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveExerciseFromWorkout
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveExerciseFromWorkout.Companion.REMOVE_WORKOUT_EXERCISE_ARE_YOU_SURE
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveExerciseFromWorkout.Companion.REMOVE_WORKOUT_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveWorkout
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveWorkout.Companion.DELETE_WORKOUT_ARE_YOU_SURE
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveWorkout.Companion.DELETE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.workout.UpdateWorkout.Companion.UPDATE_WORKOUT_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workout.UpdateWorkout.Companion.UPDATE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentWorkoutDetailBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise.WorkoutExercisesAdapter
import com.mikolove.allmightworkout.framework.presentation.main.workout_detail.WorkoutInteractionState.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutDetailFragment:
    BaseFragment(R.layout.fragment_workout_detail),
    WorkoutExercisesAdapter.Interaction {

    val viewModel : WorkoutDetailViewModel by viewModels()

    private var binding : FragmentWorkoutDetailBinding? = null
    private var listAdapter : WorkoutExercisesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*       sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.main_fragment_container
            duration = 300.toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.backgroundColor))
        }*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setHasOptionsMenu(true)
        binding = FragmentWorkoutDetailBinding.bind(view)

        setupRecyclerView()
        setupOnBackPressDispatcher()
        subscribeObservers()

        setupUI()

        binding?.fragmentManageWorkoutEditName?.setOnClickListener{
            onClickWorkoutEditName()
        }

        binding?.fragmentManageWorkoutSwitchIsActive?.setOnClickListener {
            onClickWorkoutIsActive()
        }

        binding?.fragmentManageWorkoutButtonAddExercise?.setOnClickListener {
            findNavController().navigate(R.id.action_workout_detail_fragment_to_add_exercise_to_workout_fragment)
        }

        binding?.fragmentManageWorkoutButtonLaunch?.setOnClickListener {
            viewModel.state.value?.workout?.idWorkout?.let { idWorkout ->
                val action = WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToWorkoutInProgressFragment(idWorkout)
                findNavController().navigate(action)
            }
        }
    }


    override fun onDestroyView() {
        //binding?.fragmentManageWorkoutRecyclerviewExercise?.adapter = null
        listAdapter = null
        binding = null
        super.onDestroyView()
    }

    /*******************************************************************
    MENU INTERACTIONS
     ********************************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workout_detail, menu)

        val menuValidate = menu.findItem(R.id.toolbar_manage_workout_validate)
        menuValidate.isVisible = viewModel.state.value?.isUpdatePending ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.toolbar_manage_workout_validate -> {
                if (viewModel.checkEditState()) {
                    quitEditState()
                }
                viewModel.onTriggerEvent(WorkoutDetailEvents.UpdateWorkout)
                true
            }
            R.id.toolbar_manage_workout_delete -> {
                deleteWorkout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*******************************************************************
    SETUP
     ********************************************************************/

    private fun setupUI(){
        binding?.fragmentManageWorkoutEditName?.disableContentInteraction()
    }

    private fun setupRecyclerView(){
        binding?.fragmentManageWorkoutRecyclerviewExercise?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            listAdapter = WorkoutExercisesAdapter(this@WorkoutDetailFragment)
            adapter = listAdapter
        }
    }




    /*******************************************************************
    OBSERVERS
     ********************************************************************/

    private fun subscribeObservers(){


        viewModel.state.observe(viewLifecycleOwner, { state ->

            state.isLoading?.let { uiController.displayProgressBar(it) }

            if(state.queue.peek()?.description == DELETE_WORKOUT_SUCCESS){
                findNavController().popBackStack()
            }else{
                processQueue(
                    context = context,
                    queue = state.queue,
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromQueue() {
                            viewModel.onTriggerEvent(WorkoutDetailEvents.OnRemoveHeadFromQueue)
                        }
                    })
            }
            printLogD("WorkoutDetailFragment","${state.workout}")
            state.workout?.let { workout ->

                printLogD("WorkoutDetailFragment","${workout}")
                setWorkoutUi(workout)

                listAdapter?.apply {
                    workout.exercises?.let { exercises ->
                        submitList(exercises)
                    }
                    if(itemCount >0){
                        showList()
                    }else{
                        hideList()
                    }
                }
            }
            state.isUpdatePending.let {
                requireActivity().invalidateOptionsMenu()
            }
        })

/*
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null){

                viewState.workoutSelected?.let { workout ->
                    printLogD("WorkoutDetailFragment","Workout selected load UI")
                    setWorkoutUi(workout)

                    workout?.exercises?.let { exercises ->
                        if (exercises.isNotEmpty()) {
                            listAdapter?.submitList(exercises)
                            showList()
                        } else {
                            hideList()
                        }
                    }?: hideList()
                }

                viewState.isUpdatePending?.let { isUpdatePending ->
                    activity?.invalidateOptionsMenu()
                }
            }
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

                    REMOVE_WORKOUT_EXERCISE_SUCCESS -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
                        reloadWorkoutSelected()
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
*/

        viewModel.workoutNameInteractionState.observe(viewLifecycleOwner, Observer { state ->

            when(state){

                is EditState -> {
                    binding?.fragmentManageWorkoutEditName?.enableContentInteraction()
                    view?.showKeyboard()
                    viewModel.onTriggerEvent(WorkoutDetailEvents.OnUpdateIsPending(true))
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
                    viewModel.onTriggerEvent(WorkoutDetailEvents.OnUpdateIsPending(true))                }
                is DefaultState -> { }
            }
        })

    }

    /*******************************************************************
    SETTERS - GETTERS - ACTIONS
     ********************************************************************/

    private fun deleteWorkout() {

        val message = GenericMessageInfo.Builder()
            .id("WorkoutDetailFragment.DeleteWorkout")
            .title(DELETE_WORKOUT_ARE_YOU_SURE)
            .description("")
            .messageType(MessageType.Success)
            .uiComponentType(UIComponentType.Dialog)
            .positive(
                PositiveAction(
                    positiveBtnTxt = "OK",
                    onPositiveAction = {
                        viewModel.onTriggerEvent(WorkoutDetailEvents.RemoveWorkout)
                    }
                )
            )
            .negative(
                NegativeAction(
                    negativeBtnTxt = "Cancel",
                    onNegativeAction = {}
                )
            )

        launchDialog(message)

    }
    private fun launchDialog( message : GenericMessageInfo.Builder){
        viewModel.onTriggerEvent(WorkoutDetailEvents.LaunchDialog(message))
    }

    private fun removeExerciseFromWorkout(item : Exercise) {

        val message = GenericMessageInfo.Builder()
            .id("WorkoutDetailFragment.RemoveExercise")
            .title(REMOVE_WORKOUT_EXERCISE_ARE_YOU_SURE)
            .description("")
            .messageType(MessageType.Success)
            .uiComponentType(UIComponentType.Dialog)
            .positive(
                PositiveAction(
                    positiveBtnTxt = "OK",
                    onPositiveAction = {
                        viewModel.onTriggerEvent(WorkoutDetailEvents.DeleteExercise(item.idExercise))
                    }
                )
            )
            .negative(
                NegativeAction(
                    negativeBtnTxt = "Cancel",
                    onNegativeAction = {}
                )
            )

        launchDialog(message)
    }

    private fun quitEditState(){
        updateNameInViewModel()
        updateIsActiveInViewModel()
        viewModel.exitEditState()
    }

    private fun showList(){
        if(binding?.fragmentManageWorkoutRecyclerviewExercise?.isVisible == false) {
            binding?.fragmentManageWorkoutRecyclerviewExercise?.fadeIn()
            binding?.fragmentManageWorkoutNoExercises?.fadeOut()
        }
    }

    private fun hideList(){
        if(binding?.fragmentManageWorkoutRecyclerviewExercise?.isVisible == true){
            binding?.fragmentManageWorkoutRecyclerviewExercise?.fadeOut()
            binding?.fragmentManageWorkoutNoExercises?.fadeIn()
        }
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
            viewModel.onTriggerEvent(
                WorkoutDetailEvents.OnUpdateWorkout(getWorkoutName(),getWorkoutIsActive())
            )
        }
    }
    private fun updateIsActiveInViewModel(){
        if(viewModel.isEditingIsActive()){
            viewModel.onTriggerEvent(
                WorkoutDetailEvents.OnUpdateWorkout(getWorkoutName(),getWorkoutIsActive())
            )
        }
    }

    private fun getWorkoutName() : String{
        return binding?.fragmentManageWorkoutEditName?.text.toString()
    }

    private fun getWorkoutIsActive() : Boolean{
        return binding?.fragmentManageWorkoutSwitchIsActive?.isChecked ?: false
    }

    /*******************************************************************
    Adapter interaction
     ********************************************************************/

    override fun onClickDelete(item: Exercise) {
        removeExerciseFromWorkout(item)
    }

    /*******************************************************************
    BACK BUTTON PRESS
     ********************************************************************/

    private fun onBackPressed() {
        printLogD("ManageWorkoutFragment","ON BACK PRESSED")
        if (viewModel.checkEditState()) {
            quitEditState()
        }else{
            findNavController().popBackStack()
        }
    }

    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}