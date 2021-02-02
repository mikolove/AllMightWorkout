package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutExerciseDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutExerciseDaoServiceImpl
@Inject
constructor(
    private val workoutExerciseDao: WorkoutExerciseDao
) : WorkoutExerciseDaoService{

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int {
        return workoutExerciseDao.isExerciseInWorkout(idWorkout,idExercise)
    }

    override suspend fun addExerciseToWorkout(workoutId: String, exerciseId: String): Long {
        return workoutExerciseDao.addExerciseToWorkout(workoutId,exerciseId)
    }

    override suspend fun removeExerciseFromWorkout(workoutId: String, exerciseId: String): Int {
        return workoutExerciseDao.removeExerciseFromWorkout(workoutId,exerciseId)
    }
}