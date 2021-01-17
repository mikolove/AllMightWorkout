package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutType

interface   WorkoutTypeNetworkDataSource {

    suspend fun getWorkoutTypes() : List<WorkoutType>

}