package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.ExerciseDao
import com.mikolove.allmightworkout.framework.datasource.cache.database.returnOrderedQuery
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseWithSetsCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import javax.inject.Inject
import javax.inject.Singleton

class ExerciseDaoServiceImpl
constructor(
    private val exerciseDao : ExerciseDao,
    private val exerciseCacheMapper: ExerciseCacheMapper,
    private val exerciseWithSetCacheMapper: ExerciseWithSetsCacheMapper,
    private val roomDateUtil: RoomDateUtil
) : ExerciseDaoService{

    override suspend fun insertExercise(exercise: Exercise): Long {
        return exerciseDao.insertExercise(exerciseCacheMapper.mapToEntity(exercise))
    }

    override suspend fun updateExercise(
        primaryKey: String,
        name: String,
        bodyPart: BodyPart?,
        isActive: Boolean,
        exerciseType: String,
        updatedAt: String
    ): Int {
        return exerciseDao.updateExercise(
            primaryKey = primaryKey,
            name = name,
            idBodyPart = bodyPart?.idBodyPart,
            isActive = isActive,
            exerciseType  = exerciseType,
            updatedAt = roomDateUtil.convertStringDateToDate(updatedAt)
        )
    }

    override suspend fun removeExerciseById(primaryKey: String): Int {
        return exerciseDao.removeExerciseById(primaryKey)
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise? {
        return exerciseDao.getExerciseById(primaryKey)?.let {
            exerciseWithSetsCacheEntity ->
                exerciseWithSetCacheMapper.mapFromEntity(exerciseWithSetsCacheEntity)
        }
    }

    override suspend fun getTotalExercises(): Int {
        return exerciseDao.getTotalExercises()
    }

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? {
        return exerciseDao.getExercisesByWorkout(idWorkout)?.let {
            it->
                exerciseWithSetCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun removeExercises(exercise: List<Exercise>): Int {
        val ids = exercise.mapIndexed { index, exercise -> exercise.idExercise }
        return exerciseDao.removeExercises(ids)
    }

    override suspend fun getExercises(): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercises()
        )
    }

    override suspend fun getExercisesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByDateDESC(query, page)
        )
    }

    override suspend fun getExercisesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByDateASC(query, page)
        )
    }

    override suspend fun getExercisesOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByNameDESC(query, page)
        )
    }

    override suspend fun getExercisesOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByNameASC(query, page)
        )
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.returnOrderedQuery(query, filterAndOrder, page)
        )
    }
}