package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutInProgressFragment(): BaseFragment(R.layout.fragment_base){

    val inProgressViewModel : WorkoutInProgressViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}