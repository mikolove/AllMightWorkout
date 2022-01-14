package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistory
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistory.Companion.INSERT_HISTORY_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistory.Companion.INSERT_HISTORY_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentWorkoutInProgressBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.exerciseinprogress.ExerciseInProgressEvents
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutInProgressFragment():
    BaseFragment(R.layout.fragment_workout_in_progress),
    WorkoutInProgressAdapter.Interaction{

    val viewModel : WorkoutInProgressViewModel by viewModels()

    @Inject
    lateinit var dateUtil: DateUtil

    //val args: WorkoutInProgressFragmentArgs by navArgs()
    var binding : FragmentWorkoutInProgressBinding? = null
    var listAdapter : WorkoutInProgressAdapter? = null

    override fun onDestroyView() {
        binding = null
        listAdapter = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentWorkoutInProgressBinding.bind(view)

        setupRecyclerView()
        setupOnBackPressDispatcher()
        subscribeObservers()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Exercise>(
            EXERCISE_UPDATED
        )?.observe(viewLifecycleOwner) { shouldRefresh ->
            shouldRefresh?.run {
                viewModel.onTriggerEvent(WorkoutInProgressEvents.UpdateExercise(exercise = this))
                viewModel.onTriggerEvent(WorkoutInProgressEvents.UpdateWorkoutDone)
                //findNavController().currentBackStackEntry?.savedStateHandle?.set(EXERCISE_UPDATED, null)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Exercise>(SHOULD_REFRESH)
            }
        }


        binding?.wipWorkoutButtonEnd?.setOnClickListener {
            viewModel.state.value?.isWorkoutDone?.let { isDone ->
                if(isDone){
                    quitWorkoutAndSave()
                }else{
                    areYouSureToQuitWorkout()
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                areYouSureToQuitWorkout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeObservers(){


        viewModel.state.observe(viewLifecycleOwner,{ state ->

            state.isLoading?.let { uiController.displayProgressBar(it) }

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object: StateMessageCallback {
                    override fun removeMessageFromQueue() {
                        viewModel.onTriggerEvent(WorkoutInProgressEvents.OnRemoveHeadFromQueue)
                    }
                })

            state.workout?.let { workout ->

                setupUI(workout.name,workout?.startedAt)

                workout.exercises?.let { exercises ->
                    listAdapter?.apply {
                        submitList(exercises)
                    }
                }
            }

            state.exitWorkout?.let { exitWorkout ->
                if(exitWorkout){
                    onBackPressed()
                }
            }
        })

       /* viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null) {

                viewState.workout?.let { workout ->
                    if(getExerciseList().isEmpty()){
                        workout.exercises?.let { viewModel.setExerciseList(it) }
                    }
                    setupUI()
                }

                viewState.exerciseList?.let { exercises ->
                        listAdapter?.submitList(exercises)
                }

            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { isLoading ->
            uiController.displayProgressBar(isLoading)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when(response.message){


                    INSERT_HISTORY_SUCCESS -> {

                        //OnBackPress
                        showToastHistorySaved(INSERT_HISTORY_SUCCESS)
                        onBackPressed()

                    }

                    INSERT_HISTORY_FAILED -> {
                        showToastHistorySaved(INSERT_HISTORY_FAILED)
                        onBackPressed()

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
                    }
                }
            }

        })*/
    }

    private fun setupUI(name : String, startedAt : String?){
        binding?.wipWorkoutTitle?.text = name
        binding?.wipWorkoutCreatedAt?.text = getString(
            R.string.wip_started_at,
            startedAt)
            .replaceFirstChar { it.uppercase() }
    }

    private fun setupRecyclerView(){
        binding?.wipWorkoutRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            listAdapter = WorkoutInProgressAdapter(
                this@WorkoutInProgressFragment,
                dateUtil
            )

           listAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = listAdapter
        }
    }


    private fun quitWorkoutAndSave(){
        viewModel.onTriggerEvent(WorkoutInProgressEvents.UpdateWorkoutEnded)
        viewModel.onTriggerEvent(WorkoutInProgressEvents.InsertHistory)
    }

    private fun navigateToExercise(idExercise : String){

        val bundle = bundleOf("idExercise" to idExercise)
        findNavController().navigate(R.id.action_workoutInProgressFragment_to_exerciseInProgressFragment,bundle)
    }

    override fun onItemSelected(item: Exercise) {
        if(item.endedAt == null){
            navigateToExercise(item.idExercise)
        }
    }

    private fun saveWorkout(workout : Workout, exercises : List<Exercise>){

        //viewModel.saveWorkout(workout, exercises)
    }

    private fun areYouSureToQuitWorkout(){

        val message = GenericMessageInfo.Builder()
            .id("WorkoutInProgressFragment.AreYouSureToQuitWorkout")
            .title("Abort workout")
            .description(WIP_ARE_YOU_SURE_STOP_EXERCISE)
            .messageType(MessageType.Info)
            .uiComponentType(UIComponentType.Dialog)
            .positive(
                PositiveAction(
                    positiveBtnTxt = "Save",
                    onPositiveAction = {
                        quitWorkoutAndSave()
                    }
                )
            )
            .negative(
                NegativeAction(
                    negativeBtnTxt = "Abort workout",
                    onNegativeAction = {
                        onBackPressed()
                    }
                )
            )
        launchDialog(message)
    }

    private fun launchDialog(message : GenericMessageInfo.Builder){
        viewModel.onTriggerEvent(WorkoutInProgressEvents.LaunchDialog(message))
    }

    private fun areYouSureToQuitWithoutSaving(){
        /*viewModel.setStateEvent(
            WorkoutInProgressStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = WIP_ARE_YOU_SURE_QUIT_NO_SAVE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    onBackPressed()
                                }

                                override fun cancel() {
                                }
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )*/
    }

    private fun showToastHistorySaved(text : String){

 /*       uiController.onResponseReceived(
            response = Response(
                message = text,
                uiComponentType = UIComponentType.Toast(),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object: StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )*/
    }


    /********************************************************************
    BACK BUTTON PRESS
     *********************************************************************/

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

    private fun setupOnBackPressDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                areYouSureToQuitWorkout()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}