package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutExerciseDao
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutExerciseCacheEntity

class WorkoutExerciseDaoServiceImpl
constructor(
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val dateUtil : DateUtil
) : WorkoutExerciseDaoService{

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int {
        return workoutExerciseDao.isExerciseInWorkout(idWorkout,idExercise)
    }

    override suspend fun addExerciseToWorkout(workoutId: String, exerciseId: String): Long {
        val entity = WorkoutExerciseCacheEntity(workoutId, exerciseId, dateUtil.getCurrentDate())
        return workoutExerciseDao.addExerciseToWorkout(entity)
    }

    override suspend fun removeExerciseFromWorkout(workoutId: String, exerciseId: String): Int {
        return workoutExerciseDao.removeExerciseFromWorkout(workoutId,exerciseId)
    }
}