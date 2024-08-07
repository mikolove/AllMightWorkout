/*
package com.mikolove.allmightworkout.framework.presentation.main.exercise_set_detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mikolove.allmightworkout.R
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.core.interactors.workout.RemoveMultipleWorkouts
import com.mikolove.allmightworkout.databinding.FragmentExerciseSetDetailBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise_detail.ExerciseDetailEvents
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseSetDetailFragment : BaseFragment(R.layout.fragment_exercise_set_detail) {

    val viewModel : ExerciseSetDetailViewModel by viewModels()

    private var binding : FragmentExerciseSetDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentExerciseSetDetailBinding.bind(view)

        setupAction()
        setupOnBackPressDispatcher()
        subscribeObservers()
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeObservers(){

        viewModel.state.observe(viewLifecycleOwner, { state ->

            if(state.loadInitialValues){
                state.set?.let {
                    setupUI(it)
                }
                state.exerciseType?.let {
                    setupActiveUI(it)
                }
                viewModel.onTriggerEvent(ExerciseSetDetailEvents.UpdateLoadInitialValues(false))
            }

        })

        viewModel.repInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is ExerciseSetInteractionState.EditState -> {
                    setUpdatePending()
                }

                is ExerciseSetInteractionState.DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextRep?.clearFocus()
                }
            }
        })

        viewModel.weightInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is ExerciseSetInteractionState.EditState -> {
                    setUpdatePending()
                }

                is ExerciseSetInteractionState.DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextWeight?.clearFocus()
                }
            }
        })

        viewModel.timeInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is ExerciseSetInteractionState.EditState -> {
                    setUpdatePending()
                }

                is ExerciseSetInteractionState.DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextTime?.clearFocus()
                }
            }
        })

        viewModel.restInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is ExerciseSetInteractionState.EditState -> {
                    setUpdatePending()
                }

                is ExerciseSetInteractionState.DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextRest?.clearFocus()
                }
            }
        })

    }

    private fun setupUI(set : ExerciseSet){
        setRep(set.reps)
        setWeight(set.weight)
        setTime(set.time)
        setRest(set.restTime)
    }

    private fun setupActiveUI(exerciseType : ExerciseType){
        when(exerciseType){

            ExerciseType.REP_EXERCISE -> {

                binding?.fragmentExerciseSetDetailTextTime?.editText?.isEnabled = false
                binding?.fragmentExerciseSetDetailTextRep?.editText?.isEnabled = true
            }

            ExerciseType.TIME_EXERCISE -> {

                binding?.fragmentExerciseSetDetailTextRep?.editText?.isEnabled = false
                binding?.fragmentExerciseSetDetailTextTime?.editText?.isEnabled = true
            }
        }
    }

    private fun setRep(rep : Int){
        binding?.fragmentExerciseSetDetailTextRep?.editText?.setText(rep.toString())
    }

    private fun setWeight(weight : Int){
        binding?.fragmentExerciseSetDetailTextWeight?.editText?.setText(weight.toString())
    }

    private fun setTime(time : Int){
        binding?.fragmentExerciseSetDetailTextTime?.editText?.setText(time.toString())
    }

    private fun setRest(rest : Int){
        binding?.fragmentExerciseSetDetailTextRest?.editText?.setText(rest.toString())
    }

    private fun setupAction(){

        binding?.fragmentExerciseSetDetailTextRep?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickRep()
                }
            }
        })


        binding?.fragmentExerciseSetDetailTextWeight?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickWeight()
                }
            }
        })

        binding?.fragmentExerciseSetDetailTextRest?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickRest()
                }
            }
        })

        binding?.fragmentExerciseSetDetailTextTime?.editText?.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus) {
                    onClickTime()
                }
            }
        })
    }

    private fun onClickRep(){
        if(!viewModel.isEditingRep()){
            updateSetRestInViewModel()
            updateSetTimeInViewModel()
            updateSetWeightInViewModel()
            viewModel.setInteractionRepState(ExerciseSetInteractionState.EditState())
        }
    }

    private fun onClickWeight(){
        if(!viewModel.isEditingWeight()){
            updateSetTimeInViewModel()
            updateSetRepInViewModel()
            updateSetRestInViewModel()
            viewModel.setInteractionWeightState(ExerciseSetInteractionState.EditState())
        }
    }
    private fun onClickRest(){
        if(!viewModel.isEditingRest()){
            updateSetRepInViewModel()
            updateSetTimeInViewModel()
            updateSetWeightInViewModel()
            viewModel.setInteractionRestState(ExerciseSetInteractionState.EditState())
        }
    }

    private fun onClickTime(){
        if(!viewModel.isEditingTime()){
            updateSetRepInViewModel()
            updateSetRestInViewModel()
            updateSetWeightInViewModel()
            viewModel.setInteractionTimeState(ExerciseSetInteractionState.EditState())
        }
    }

    private fun updateSetRepInViewModel(){
        if(viewModel.isEditingRep()){
            viewModel.onTriggerEvent(ExerciseSetDetailEvents.UpdateReps(getRep()))
        }
    }

    private fun updateSetTimeInViewModel(){
        if(viewModel.isEditingTime()){
            viewModel.onTriggerEvent(ExerciseSetDetailEvents.UpdateTime(getTime()))
        }
    }

    private fun updateSetWeightInViewModel(){
        if(viewModel.isEditingWeight()){
            viewModel.onTriggerEvent(ExerciseSetDetailEvents.UpdateWeight(getWeight()))
        }
    }

    private fun updateSetRestInViewModel(){
        if(viewModel.isEditingRest()){
            viewModel.onTriggerEvent(ExerciseSetDetailEvents.UpdateRestTime(getRest()))
        }
    }

    private fun getRep() : Int{
        val text = binding?.fragmentExerciseSetDetailTextRep?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 8
            else -> value
        }
    }

    private fun getTime(): Int{
        val text = binding?.fragmentExerciseSetDetailTextTime?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 60
            else -> value
        }
    }

    private fun getRest(): Int{
        val text = binding?.fragmentExerciseSetDetailTextRest?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 60
            else -> value
        }
    }

    private fun getWeight(): Int{
        val text = binding?.fragmentExerciseSetDetailTextWeight?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 5
            else -> value
        }
    }

    private fun setUpdatePending(){
        viewModel.onTriggerEvent(ExerciseSetDetailEvents.OnUpdateIsPending(true))
    }

    */
/********************************************************************
    BACK BUTTON PRESS
     *********************************************************************//*


    private fun onBackPressed() {

        updateSetRepInViewModel()
        updateSetWeightInViewModel()
        updateSetRestInViewModel()
        updateSetTimeInViewModel()
        view?.hideKeyboard()
        viewModel.exitSetEditState()

        viewModel.state.value?.let { state ->
            if(state.isUpdatePending){
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    EXERCISE_SET_UPDATED,
                    state.set
                )
            }
        }
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            SHOULD_REFRESH,
            true
        )
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

}*/
