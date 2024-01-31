package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutExerciseDao
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutWithExercisesCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutExerciseCacheEntity
import javax.inject.Inject
import javax.inject.Singleton

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