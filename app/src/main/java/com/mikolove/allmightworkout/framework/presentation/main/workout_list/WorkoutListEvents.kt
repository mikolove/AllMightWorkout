package com.mikolove.allmightworkout.framework.presentation.main.workout_list

import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo

sealed class WorkoutListEvents {

    object NewSearch : WorkoutListEvents()

    object NextPage : WorkoutListEvents()

    data class UpdateQuery(val query : String) : WorkoutListEvents()

    data class UpdateFilter(val filter : WorkoutFilterOptions) : WorkoutListEvents()

    data class UpdateOrder(val order : WorkoutOrderOptions) : WorkoutListEvents()

    object RemoveSelectedWorkouts : WorkoutListEvents()

    object GetOrderAndFilter : WorkoutListEvents()

    data class InsertWorkout(val name : String) : WorkoutListEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : WorkoutListEvents()

    data class Error(val message: GenericMessageInfo.Builder): WorkoutListEvents()

    object OnRemoveHeadFromQueue: WorkoutListEvents()
}