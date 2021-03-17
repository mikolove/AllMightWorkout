package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.business.interactors.main.workout.AddExerciseToWorkout.Companion.INSERT_WORKOUT_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveExerciseFromWorkout.Companion.REMOVE_WORKOUT_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.databinding.FragmentAddExerciseToWorkoutBinding
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.common.TopSpacingItemDecoration
import com.mikolove.allmightworkout.framework.presentation.common.fadeIn
import com.mikolove.allmightworkout.framework.presentation.common.fadeOut
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseSetInteractionState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExerciseToWorkoutFragment():
    BaseFragment(R.layout.fragment_add_exercise_to_workout),
    AddExerciseAdapter.Interaction{

    val viewModel : WorkoutViewModel by activityViewModels()

    private var listAdapter : AddExerciseAdapter? = null
    private var binding : FragmentAddExerciseToWorkoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        binding = FragmentAddExerciseToWorkoutBinding.bind(view)

        init()
        setupOnBackPressDispatcher()
        setupRecyclerView()
        setupSwipeRefresh()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTotalExercises()
        viewModel.refreshExerciseSearchQuery()
    }

    override fun onDestroyView() {
        binding?.fragmentAddExerciseToWorkoutRecyclerview?.adapter = null
        listAdapter = null
        binding = null
        super.onDestroyView()
    }

    /********************************************************************
        OBSERVERS
     *********************************************************************/

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {

                viewState.listExercises?.let { exercises ->

                    if (exercises.isNotEmpty()) {
                        if (viewModel.isExercisesPaginationExhausted() && !viewModel.isExercisesQueryExhausted()) {
                            viewModel.setExerciseQueryExhausted(true)
                        }

                        listAdapter?.submitList(exercises)
                        showList()
                    } else {
                        hideList()
                    }
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, { state ->
            uiController.displayProgressBar(state)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                when (response.message) {

                    INSERT_WORKOUT_EXERCISE_SUCCESS -> {
                        uiController.onResponseReceived(
                            response = stateMessage.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                        )
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
                    }

                    else ->{
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

    /********************************************************************
        Setup UI
     *********************************************************************/

    private fun init(){
        viewModel?.getWorkoutSelected()?.let { workout ->
            workout.exercises?.forEach { exercise ->
                viewModel.addOrRemoveExerciseFromSelectedList(exercise)
            }
        }
    }

    private fun showList(){
        if(binding?.fragmentAddExerciseToWorkoutSwiperefreshlayout?.isVisible == false) {
            binding?.fragmentAddExerciseToWorkoutSwiperefreshlayout?.fadeIn()
            binding?.fragmentAddExerciseToWorkoutNoContent?.fadeOut()
        }
    }

    private fun hideList(){
        if(binding?.fragmentAddExerciseToWorkoutSwiperefreshlayout?.isVisible == true){
            binding?.fragmentAddExerciseToWorkoutSwiperefreshlayout?.fadeOut()
            binding?.fragmentAddExerciseToWorkoutNoContent?.fadeIn()
        }
    }

    private fun setupSwipeRefresh(){
        binding?.fragmentAddExerciseToWorkoutSwiperefreshlayout?.setOnRefreshListener {
            startNewSearch()
            binding?.fragmentAddExerciseToWorkoutSwiperefreshlayout?.isRefreshing = false
        }
    }
    private fun setupRecyclerView(){
        binding?.fragmentAddExerciseToWorkoutRecyclerview?.apply {

            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)

            listAdapter = AddExerciseAdapter(
                this@AddExerciseToWorkoutFragment,
                viewLifecycleOwner,
                viewModel.exerciseListInteractionManager.selectedItems
            )


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == listAdapter?.itemCount?.minus(1)) {
                        viewModel.nextPageExercises()
                    }
                }
            })
            adapter = listAdapter
        }
    }

    /********************************************************************
        MENU INTERACTIONS
     *********************************************************************/

    private fun startNewSearch(){
        //viewModel.clearListExercises()
        viewModel.exercisesStartNewSearch()
    }

    /********************************************************************
        MENU INTERACTIONS
     *********************************************************************/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackPressed() {
        viewModel.updateWorkoutExercises(viewModel.getSelectedExercises())
        viewModel.clearSelectedExercises()
        findNavController().popBackStack()
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
        ADAPTER INTERACTIONS
     *********************************************************************/

    private fun addExerciseToWorkout(exercise: Exercise){
        val idWorkout = viewModel.getWorkoutSelected()?.idWorkout
        idWorkout?.let {
            viewModel.addExerciseToWorkout(
                exercise.idExercise,
                idWorkout)
        }
    }

    private fun removeExerciseFromWorkout(exercise: Exercise){
        val idWorkout = viewModel.getWorkoutSelected()?.idWorkout
        idWorkout?.let {
            viewModel.removeExerciseFromWorkout(
                exercise.idExercise,
                idWorkout)
        }
    }

    override fun onItemSelected(item: Exercise) {
        viewModel.addOrRemoveExerciseFromSelectedList(item)
        if(isExerciseSelected(item)){
            printLogD("AddExercise","remove")
            addExerciseToWorkout(item)

        }else{
            removeExerciseFromWorkout(item)
            printLogD("AddExercise","add")
        }
    }

    override fun isExerciseSelected(exercise: Exercise): Boolean {
        return viewModel.isExerciseSelected(exercise)
    }

}