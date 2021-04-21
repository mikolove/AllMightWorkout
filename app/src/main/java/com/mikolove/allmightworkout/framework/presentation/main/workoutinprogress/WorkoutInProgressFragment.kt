package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.databinding.FragmentWorkoutInProgressBinding
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.TopSpacingItemDecoration
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
        subscribeObservers()

    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if(viewState != null) {

                viewState.workout?.let { workout ->

                    if(getExerciseList().isEmpty()){
                        workout.exercises?.let { viewModel.setExerciseList(it) }
                        setupUI()
                    }

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

    private fun navigateToExercise(item : Exercise){

        val updateExercise = item.copy(
            startedAt = dateUtil.getCurrentTimestamp()
        )
        viewModel.setExercise(updateExercise)

        findNavController().navigate(R.id.action_workoutInProgressFragment_to_exerciseInProgressFragment)
    }

    override fun onItemSelected(item: Exercise) {
        navigateToExercise(item)
    }
}