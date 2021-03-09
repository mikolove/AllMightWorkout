package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.databinding.ItemSetBinding
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseSetInteractionState

class ExerciseSetDetailFragment : BaseFragment(R.layout.fragment_exercise_set_detail) {

    val viewModel : ExerciseViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }


/*
    private fun onClickRep(set : ExerciseSet){
        if(!isEditingRep()){
            updateSetInViewModel(set,binding)
            setInteractionRepState(ExerciseSetInteractionState.EditState())
        }
    }

    private fun onClickWeight(set : ExerciseSet){
        if(!isEditingWeight()){
            updateSetInViewModel(set,binding)
            setInteractionWeightState(ExerciseSetInteractionState.EditState())
        }
    }
    private fun onClickRest(set: ExerciseSet){
        if(!isEditingRest()){
            updateSetInViewModel(set,binding)
            setInteractionRestState(ExerciseSetInteractionState.EditState())
        }
    }

    private fun onClickTime(set : ExerciseSet){
        if(!isEditingTime()){
            updateSetInViewModel(set,binding)
            setInteractionTimeState(ExerciseSetInteractionState.EditState())
        }
    }

    private fun updateSetInViewModel(set : ExerciseSet, binding: ItemSetBinding){
        set.reps = binding?.itemSetTextRep?.editText?.text.toString().toInt()
        set.restTime = binding?.itemSetTextRest?.editText?.text.toString().toInt()
        set.time = binding?.itemSetTextRest?.editText?.text.toString().toInt()
        set.weight = binding?.itemSetTextWeight?.editText?.text.toString().toInt()
        interaction?.updateSetInViewModel(set)
    }*/

}