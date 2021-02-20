package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExerciseToWorkoutFragment(): BaseFragment(R.layout.fragment_base){

    val viewModel : WorkoutViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}