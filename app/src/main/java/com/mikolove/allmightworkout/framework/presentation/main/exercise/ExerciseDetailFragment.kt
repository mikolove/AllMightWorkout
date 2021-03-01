package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.StateMessageCallback
import com.mikolove.allmightworkout.business.interactors.main.exercise.GetExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises
import com.mikolove.allmightworkout.databinding.FragmentExerciseBinding
import com.mikolove.allmightworkout.databinding.FragmentExerciseDetailBinding
import com.mikolove.allmightworkout.framework.presentation.FabController
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseDetailFragment():
    BaseFragment(R.layout.fragment_exercise_detail),
    ExerciseSetListAdapter.Interaction,
    FabController{

    val viewModel : ExerciseViewModel by activityViewModels()

    private var binding : FragmentExerciseDetailBinding? = null
    private var exerciseSetAdapter : ExerciseSetListAdapter? = null
    private var workoutTypeAdapter : ArrayAdapter<WorkoutType>? = null
    private var bodyPartAdapter : ArrayAdapter<BodyPart>? = null
    private var exerciseTypeAdapter : ArrayAdapter<ExerciseType>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseDetailBinding.bind(view)

        ///setupUI()
        //setupDropdown()
        //setupRecyclerView()
        setupAdapters()
        subscribeObservers()

    }

    override fun onDestroyView() {

        exerciseSetAdapter = null
        workoutTypeAdapter = null
        bodyPartAdapter = null
        exerciseTypeAdapter = null
        binding = null

        super.onDestroyView()
    }

    /*
        Observers
     */

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner,{ viewState ->

            if(viewState != null){

                viewState.listWorkoutTypes?.let { workoutTypeList ->
                    workoutTypeAdapter?.addAll(workoutTypeList)
                }

                viewState.listBodyParts?.let {bodyPartList ->
                    bodyPartAdapter?.addAll(bodyPartList)
                }
            }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.response?.let { response ->

                uiController.onResponseReceived(
                    response = stateMessage.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    })
            }
        })
    }

    /*
        setup
     */

    private fun setupAdapters(){

        //WorkoutTypeAdapter
        val types = viewModel.getWorkoutTypes()?.toList() ?: listOf()
        workoutTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_workouttype,
            types)

        (binding?.exerciseDetailWorkouttype?.editText as AutoCompleteTextView)?.setAdapter(workoutTypeAdapter)

        //WorkoutTypeAdapter
        val bodyParts = viewModel.getBodyParts()?.toList() ?: listOf()
        bodyPartAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_workouttype,
            bodyParts)

        (binding?.exerciseDetailBodypart?.editText as AutoCompleteTextView)?.setAdapter(bodyPartAdapter)

    }

    override fun onEditClick(expandableView: View) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(item: ExerciseSet) {
        TODO("Not yet implemented")
    }

    override fun isEditModeEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun activateEditMode() {
        TODO("Not yet implemented")
    }

    override fun setupFAB() {
        TODO("Not yet implemented")
    }

    override fun fabOnClick() {
        TODO("Not yet implemented")
    }
}