package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseSetDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.ExerciseSetDao
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseSetCacheMapper

class ExerciseSetDaoServiceImpl
constructor(
    private val exerciseSetDao: ExerciseSetDao,
    private val exerciseSetCacheMapper: ExerciseSetCacheMapper,
    private val dateUtil: DateUtil
) : ExerciseSetDaoService{

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String): Long {
        val exerciseSetCacheEntity = exerciseSetCacheMapper.mapToEntity(exerciseSet)
        exerciseSetCacheEntity.idExercise = idExercise
        return exerciseSetDao.insertExerciseSet(exerciseSetCacheEntity)
    }

    override suspend fun updateExerciseSet(
        primaryKey: String,
        reps: Int,
        weight: Int,
        time: Int,
        restTime: Int,
        order: Int,
        updatedAt: String,
        idExercise: String
    ): Int {
        return exerciseSetDao.updateExerciseSet(
            primaryKey = primaryKey,
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            order = order,
            updatedAt = dateUtil.convertStringDateToDate(updatedAt),
            idExercise = idExercise
        )
    }

    override suspend fun removeExerciseSets(exerciseSets: List<ExerciseSet>): Int {
        val ids = exerciseSets.mapIndexed{ _ ,exerciseSet -> exerciseSet.idExerciseSet }
        return exerciseSetDao.removeExerciseSets(ids)
    }

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise: String): Int {
        return exerciseSetDao.removeExerciseSetById(primaryKey,idExercise)
    }

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? {
        return exerciseSetDao.getExerciseSetById(primaryKey,idExercise).let {
            exerciseSetCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet> {
        return exerciseSetDao.getExerciseSetByIdExercise(idExercise).let {
            exerciseSetCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getTotalExercisesSetByExercise(idExercise: String): Int {
        return exerciseSetDao.getTotalExercisesSetByExercise(idExercise)
    }
}