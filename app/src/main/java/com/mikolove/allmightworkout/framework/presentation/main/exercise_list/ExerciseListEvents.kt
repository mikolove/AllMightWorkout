package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo

sealed class ExerciseListEvents {
    object NewSearch : ExerciseListEvents()

    object Refresh : ExerciseListEvents()

    object NextPage : ExerciseListEvents()

    data class UpdateQuery(val query : String) : ExerciseListEvents()

    data class UpdateFilter(val filter : ExerciseFilterOptions) : ExerciseListEvents()

    data class UpdateOrder(val order : ExerciseOrderOptions) : ExerciseListEvents()

    object RemoveSelectedExercises : ExerciseListEvents()

    object GetOrderAndFilter : ExerciseListEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : ExerciseListEvents()

    data class Error(val message: GenericMessageInfo.Builder): ExerciseListEvents()

    object OnRemoveHeadFromQueue: ExerciseListEvents()
}