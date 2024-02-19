package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup

interface WorkoutGroupDataSource {

    suspend fun getWorkoutGroups() : List<WorkoutGroup>

    suspend fun insertWorkoutGroup( workoutGroup : WorkoutGroup) : Long

    suspend fun updateWorkoutGroup(workoutGroup : WorkoutGroup) : Int

    suspend fun deleteWorkoutGroup(workoutGroups : WorkoutGroup) : Int
}