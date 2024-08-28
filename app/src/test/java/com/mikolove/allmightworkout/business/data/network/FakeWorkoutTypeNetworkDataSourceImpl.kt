package com.mikolove.allmightworkout.business.data.network

import com.mikolove.core.data.workouttype.abstraction.WorkoutTypeNetworkDataSource
import com.mikolove.core.domain.workouttype.WorkoutType

class FakeWorkoutTypeNetworkDataSourceImpl(
    private val workoutTypeDatas : HashMap<String, WorkoutType>
) : WorkoutTypeNetworkDataSource {

    override suspend fun getAllWorkoutTypes(): List<WorkoutType> {
        return ArrayList<WorkoutType>(workoutTypeDatas.values)
    }
}