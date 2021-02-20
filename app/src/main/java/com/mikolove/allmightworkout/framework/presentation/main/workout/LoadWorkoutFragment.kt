package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.framework.presentation.main.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadWorkoutFragment(): BaseFragment(R.layout.fragment_base){

    val viewModel : HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}