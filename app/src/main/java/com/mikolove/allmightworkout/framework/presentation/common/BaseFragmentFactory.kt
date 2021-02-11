package com.mikolove.allmightworkout.framework.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.presentation.main.history.HistoryDetailFragment
import com.mikolove.allmightworkout.framework.presentation.main.history.HistoryFragment
import com.mikolove.allmightworkout.framework.presentation.main.home.ChooseExerciseFragment
import com.mikolove.allmightworkout.framework.presentation.main.home.ChooseWorkoutFragment
import com.mikolove.allmightworkout.framework.presentation.main.home.HomeFragment
import com.mikolove.allmightworkout.framework.presentation.main.home.LoadWorkoutFragment
import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.ManageExerciseFragment
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.AddExerciseToWorkoutFragment
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.ManageWorkoutFragment
import com.mikolove.allmightworkout.framework.presentation.main.workout.ExerciseInProgressFragment
import com.mikolove.allmightworkout.framework.presentation.main.workout.WorkoutInProgressFragment
import com.mikolove.allmightworkout.util.printLogD
import javax.inject.Inject

//Allows constructor di of ViewModel to fragment
/*
class BaseFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dateUtil: DateUtil
) : FragmentFactory(){


    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        printLogD("BaseFragmentFactory","Called")
        return when(className){

            //Home
            HomeFragment::class.java.name -> {
                val fragment = HomeFragment(viewModelFactory)
                fragment
            }
            LoadWorkoutFragment::class.java.name -> {
                val fragment = LoadWorkoutFragment(viewModelFactory)
                fragment
            }
            ChooseWorkoutFragment::class.java.name -> {
                val fragment = ChooseWorkoutFragment(viewModelFactory)
                fragment
            }
            ChooseExerciseFragment::class.java.name -> {
                val fragment = ChooseExerciseFragment(viewModelFactory)
                fragment
            }

            //History
            HistoryFragment::class.java.name -> {
                val fragment = HistoryFragment(viewModelFactory)
                fragment
            }
            HistoryDetailFragment::class.java.name -> {
                val fragment = HistoryDetailFragment(viewModelFactory)
                fragment
            }

            //ManageExercise
            ManageExerciseFragment::class.java.name -> {
                val fragment = ManageExerciseFragment(viewModelFactory)
                fragment
            }

            //ManageWorkout
            ManageWorkoutFragment::class.java.name -> {
                val fragment = ManageWorkoutFragment(viewModelFactory)
                fragment
            }
            AddExerciseToWorkoutFragment::class.java.name -> {
                val fragment  = AddExerciseToWorkoutFragment(viewModelFactory)
                fragment
            }

            //Workout
            WorkoutInProgressFragment::class.java.name -> {
                val fragment = WorkoutInProgressFragment(viewModelFactory)
                fragment
            }
            ExerciseInProgressFragment::class.java.name -> {
                val fragment = ExerciseInProgressFragment(viewModelFactory)
                fragment
            }

            else -> super.instantiate(classLoader, className)
        }
    }


}*/
