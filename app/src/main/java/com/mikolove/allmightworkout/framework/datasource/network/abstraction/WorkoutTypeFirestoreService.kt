package com.mikolove.allmightworkout.framework.datasource.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutType

interface WorkoutTypeFirestoreService {

    suspend fun getWorkoutTypes() : List<WorkoutType>

}