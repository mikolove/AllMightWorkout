package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutTypeNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.WorkoutType

class FakeWorkoutTypeNetworkDataSourceImpl(
    private val workoutTypeDatas : HashMap<String,WorkoutType>
) : WorkoutTypeNetworkDataSource{

    override suspend fun getAllWorkoutTypes(): List<WorkoutType> {
        return ArrayList<WorkoutType>(workoutTypeDatas.values)
    }
}