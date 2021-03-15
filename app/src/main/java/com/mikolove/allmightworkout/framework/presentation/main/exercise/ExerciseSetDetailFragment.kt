package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.databinding.FragmentExerciseSetDetailBinding
import com.mikolove.allmightworkout.framework.presentation.common.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseSetInteractionState.*
import com.mikolove.allmightworkout.util.printLogD

class ExerciseSetDetailFragment : BaseFragment(R.layout.fragment_exercise_set_detail) {

    val viewModel : ExerciseViewModel by activityViewModels()

    private var binding : FragmentExerciseSetDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
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

        viewModel.viewState.observe(viewLifecycleOwner,{ viewState ->

            if(viewState != null){

                viewState.exerciseSetSelected?.let { exerciseSet ->
                    printLogD("ExerciseSetDetailFragment","${exerciseSet}")
                    setupUI(exerciseSet)
                }
            }
        })

        viewModel.exerciseTypeState.observe(viewLifecycleOwner,{ exerciseType ->
            setupActiveUI(exerciseType)
        })

        viewModel.repInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is EditState -> {
                    setUpdatePending()
                }

                is DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextRep?.clearFocus()
                }
            }
        })

        viewModel.weightInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is EditState -> {
                    setUpdatePending()
                }

                is DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextWeight?.clearFocus()
                }
            }
        })

        viewModel.timeInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is EditState -> {
                    setUpdatePending()
                }

                is DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextTime?.clearFocus()
                }
            }
        })

        viewModel.restInteractionState.observe(viewLifecycleOwner,{ state ->
            when(state){

                is EditState -> {
                    setUpdatePending()
                }

                is DefaultState ->{
                    binding?.fragmentExerciseSetDetailTextRest?.clearFocus()
                }
            }
        })

    }

    private fun setupUI(set :ExerciseSet){
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
            viewModel.setInteractionRepState(EditState())
        }
    }

    private fun onClickWeight(){
        if(!viewModel.isEditingWeight()){
            updateSetTimeInViewModel()
            updateSetRepInViewModel()
            updateSetRestInViewModel()
            viewModel.setInteractionWeightState(EditState())
        }
    }
    private fun onClickRest(){
        if(!viewModel.isEditingRest()){
            updateSetRepInViewModel()
            updateSetTimeInViewModel()
            updateSetWeightInViewModel()
            viewModel.setInteractionRestState(EditState())
        }
    }

    private fun onClickTime(){
        if(!viewModel.isEditingTime()){
            updateSetRepInViewModel()
            updateSetRestInViewModel()
            updateSetWeightInViewModel()
            viewModel.setInteractionTimeState(EditState())
        }
    }

    private fun updateSetRepInViewModel(){
        if(viewModel.isEditingRep()){
            viewModel.updateExerciseSetRep(getRep())
        }
    }

    private fun updateSetTimeInViewModel(){
        if(viewModel.isEditingTime()){
            viewModel.updateExerciseSetTime(getTime())
        }
    }

    private fun updateSetWeightInViewModel(){
        if(viewModel.isEditingWeight()){
            viewModel.updateExerciseSetWeight(getWeight())
        }
    }

    private fun updateSetRestInViewModel(){
        if(viewModel.isEditingRest()){
            viewModel.updateExerciseSetRest(getRest())
        }
    }

    private fun getRep() : Int{
        val text = binding?.fragmentExerciseSetDetailTextRep?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 0
            else -> value
        }
    }

    private fun getTime(): Int{
        val text = binding?.fragmentExerciseSetDetailTextTime?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 0
            else -> value
        }
    }

    private fun getRest(): Int{
        val text = binding?.fragmentExerciseSetDetailTextRest?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 0
            else -> value
        }
    }

    private fun getWeight(): Int{
        val text = binding?.fragmentExerciseSetDetailTextWeight?.editText?.text.toString()
        return when(val value = text.toIntOrNull()){
            null -> 0
            else -> value
        }
    }

    private fun setUpdatePending(){
        if(viewModel.isExistExercise()){
            viewModel.setIsUpdatePending(true)
        }
    }

    /********************************************************************
    BACK BUTTON PRESS
     *********************************************************************/

    private fun quitEditState(){
        updateSetRepInViewModel()
        updateSetWeightInViewModel()
        updateSetRestInViewModel()
        updateSetTimeInViewModel()
        view?.hideKeyboard()
        viewModel.exitSetEditState()
    }

    private fun onBackPressed() {
        if (viewModel.checkSetEditState()) {
            quitEditState()
        }else{
            viewModel.setExerciseSetSelected(null)
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