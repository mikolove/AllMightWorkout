package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.ExerciseDao
import com.mikolove.core.database.mappers.ExerciseCacheMapper
import com.mikolove.core.database.mappers.ExerciseWithSetsCacheMapper
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExerciseDaoServiceImpl
constructor(
    private val exerciseDao : ExerciseDao,
    private val exerciseCacheMapper: ExerciseCacheMapper,
    private val exerciseWithSetCacheMapper: ExerciseWithSetsCacheMapper,
) : ExerciseCacheService {

    override suspend fun upsertExercise(exercise: Exercise, idUser: String): Long {
        val entity = exerciseCacheMapper.mapToEntity(exercise).copy(idUser = idUser)
        return exerciseDao.upsertExercise(entity)
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise {
        return exerciseDao.getExerciseById(primaryKey).let {
            exerciseWithSetsCacheEntity ->
                exerciseWithSetCacheMapper.mapFromEntity(exerciseWithSetsCacheEntity)
        }
    }

    override suspend fun removeExercises(exercises: List<Exercise>): Int {
        val ids = exercises.mapIndexed { _, exercise -> exercise.idExercise }
        return exerciseDao.removeExercises(ids)
    }

    override suspend fun getExercises(idUser: String): Flow<List<Exercise>> {
        return exerciseDao.getExercises(idUser).map{ entities ->
            entities.map{ exerciseCacheMapper.mapFromEntity(it)}
        }
    }

}