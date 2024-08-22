package com.mikolove.core.network.abstraction

import com.mikolove.core.domain.workouttype.WorkoutType

interface WorkoutTypeFirestoreService {

    suspend fun getWorkoutTypes() : List<WorkoutType>

}