package com.mikolove.allmightworkout.firebase.abstraction

import com.mikolove.core.domain.workouttype.WorkoutType

interface WorkoutTypeFirestoreService {

    suspend fun getWorkoutTypes() : List<WorkoutType>

}