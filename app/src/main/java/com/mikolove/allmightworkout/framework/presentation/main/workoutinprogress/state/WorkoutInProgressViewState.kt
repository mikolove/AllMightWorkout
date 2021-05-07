package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
class WorkoutInProgressViewState(

    //wip
    var workout: Workout? = null,
    var isWorkoutDone : Boolean? = null,
    var exerciseList : List<Exercise>? = null,

    //eip
    var exercise: Exercise? = null,
    var setList : List<ExerciseSet>? = null,
    var actualSet : ExerciseSet? = null,

    //May Change
    var idHistoryWorkoutInserted: String? = null,
    var lastHistoryWorkoutInserted: HistoryWorkout? = null,
    var lastHistoryExerciseInserted: HistoryExercise? = null,
    var lastHistoryExerciseSetInserted: HistoryExerciseSet? = null,
    var historyExercises: ArrayList<HistoryExercise>? = null,

) : Parcelable, ViewState {
}