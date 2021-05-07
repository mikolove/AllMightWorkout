package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
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
import com.mikolove.allmightworkout.databinding.FragmentWorkoutInProgressBinding
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.TopSpacingItemDecoration
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutInProgressFragment():
    BaseFragment(R.layout.fragment_workout_in_progress),
    WorkoutInProgressAdapter.Interaction{

    val viewModel : WorkoutInProgressViewModel by activityViewModels()

    @Inject
    lateinit var dateUtil: DateUtil

    val args: WorkoutInProgressFragmentArgs by navArgs()
    var binding : FragmentWorkoutInProgressBinding? = null
    var listAdapter : WorkoutInProgressAdapter? = null

    override fun onDestroyView() {
        binding = null
        listAdapter = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentWorkoutInProgressBinding.bind(view)

        loadWorkout(getIdWorkout())

        setupRecyclerView()
        onBackPressed()
        subscribeObservers()

        binding?.wipWorkoutButtonEnd?.setOnClickListener {
            quitWorkout()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

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


        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when(response.message){

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

        })
    }

    private fun setupUI(){
        binding?.wipWorkoutCreatedAt?.text = getWorkout()?.startedAt
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

    private fun loadWorkout(idWorkout : String){
        viewModel.getWorkoutById(idWorkout)
    }

    private fun getIdWorkout() : String{
        return args.idWorkout
    }

    private fun getWorkout() : Workout? {
        return viewModel.getWorkout()
    }

    private fun getExerciseList() : List<Exercise> {
        return viewModel.getExerciseList()
    }

    private fun quitWorkout(){

        //if not finish prompt user demand
        if(viewModel.isWorkoutDone()){
            areYouSureToQuitWorkout()
        }
        //saveExercise

        //saveWorkout
        val workout = viewModel.getWorkout()
        val exerciseList = viewModel.getExerciseList()
        if(workout!=null && exerciseList != null){
            saveWorkout(workout,exerciseList)
        }

        //unbind all and quit to homescreen

    }


    private fun navigateToExercise(item : Exercise){

        val updateExercise = item.copy(
            startedAt = dateUtil.getCurrentTimestamp()
        )
        viewModel.setExercise(updateExercise)

        findNavController().navigate(R.id.action_workoutInProgressFragment_to_exerciseInProgressFragment)
    }

    override fun onItemSelected(item: Exercise) {
        if(item.endedAt == null){
            navigateToExercise(item)
        }
    }

    private fun saveWorkout(workout : Workout, exercises : List<Exercise>){

        viewModel.saveWorkout(workout, exercises)
    }

    private fun areYouSureToQuitWorkout(){
        viewModel.setStateEvent(
            WorkoutInProgressStateEvent.CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        message = WIP_ARE_YOU_SURE_STOP_EXERCISE,
                        uiComponentType = UIComponentType.AreYouSureDialog(
                            object : AreYouSureCallback {
                                override fun proceed() {
                                    //Save exercise state
                                    quitWorkout()
                                }

                                override fun cancel() {
                                    // do nothing
                                }
                            }
                        ),
                        messageType = MessageType.Info()
                    )
                )
            )
        )
    }


    /********************************************************************
    BACK BUTTON PRESS
     *********************************************************************/

    private fun onBackPressed() {
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