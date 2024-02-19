package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutGroupDataSource
import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutGroupDaoService

class WorkoutGroupDataSourceImpl(
    private val workoutGroupDaoService : WorkoutGroupDaoService
): WorkoutGroupDataSource{


    override suspend fun getWorkoutGroups(): List<WorkoutGroup> = workoutGroupDaoService.getWorkoutGroups()

    override suspend fun insertWorkoutGroup(workoutGroup: WorkoutGroup): Long  = workoutGroupDaoService.insertWorkoutGroup(workoutGroup)

    override suspend fun updateWorkoutGroup(workoutGroup: WorkoutGroup): Int =
        workoutGroupDaoService.updateWorkoutGroup(workoutGroup)

    override suspend fun deleteWorkoutGroup(workoutGroups : WorkoutGroup): Int = workoutGroupDaoService.deleteWorkoutGroup(workoutGroups)
}