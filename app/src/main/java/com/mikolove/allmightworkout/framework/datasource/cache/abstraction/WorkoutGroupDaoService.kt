package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup

interface WorkoutGroupDaoService {

    suspend fun getWorkoutGroups() : List<WorkoutGroup>

    suspend fun insertWorkoutGroup( workoutGroup : WorkoutGroup) : Long

    suspend fun updateWorkoutGroup(workoutGroup : WorkoutGroup) : Int

    suspend fun deleteWorkoutGroup(workoutGroups : WorkoutGroup) : Int
}