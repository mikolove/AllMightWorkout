package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.framework.presentation.common.BaseFragment
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseFragment(): BaseFragment(R.layout.fragment_exercise){

    val viewModel : ExerciseViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        printLogD("ChooseExerciseFragment","OnDestroyView")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}