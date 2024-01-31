package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.ExerciseDao
import com.mikolove.allmightworkout.framework.datasource.cache.database.returnOrderedQuery
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.ExerciseWithSetsCacheMapper

class ExerciseDaoServiceImpl
constructor(
    private val exerciseDao : ExerciseDao,
    private val exerciseCacheMapper: ExerciseCacheMapper,
    private val exerciseWithSetCacheMapper: ExerciseWithSetsCacheMapper,
    private val dateUtil: DateUtil
) : ExerciseDaoService{

    override suspend fun insertExercise(exercise: Exercise,idUser : String): Long {
        val entity = exerciseCacheMapper.mapToEntity(exercise).copy(idUser=idUser)
        return exerciseDao.insertExercise(entity)
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
            updatedAt = dateUtil.convertStringDateToDate(updatedAt)
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

    override suspend fun getTotalExercises(idUser: String): Int {
        return exerciseDao.getTotalExercises(idUser)
    }

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? {
        return exerciseDao.getExercisesByWorkout(idWorkout)?.let {
            it->
                exerciseWithSetCacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun removeExercises(exercises: List<Exercise>): Int {
        val ids = exercises.mapIndexed { _, exercise -> exercise.idExercise }
        return exerciseDao.removeExercises(ids)
    }

    override suspend fun getExercises(idUser: String): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercises(idUser)
        )
    }

    override suspend fun getExercisesOrderByDateDESC(
        query: String,
        page: Int,
        idUser : String
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByDateDESC(query, page, idUser)
        )
    }

    override suspend fun getExercisesOrderByDateASC(
        query: String,
        page: Int,
        idUser : String
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByDateASC(query, page, idUser)
        )
    }

    override suspend fun getExercisesOrderByNameDESC(
        query: String,
        page: Int,
        idUser : String
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByNameDESC(query, page, idUser)
        )
    }

    override suspend fun getExercisesOrderByNameASC(
        query: String,
        page: Int,
        idUser : String
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.getExercisesOrderByNameASC(query, page, idUser)
        )
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser : String
    ): List<Exercise> {
        return exerciseWithSetCacheMapper.entityListToDomainList(
            exerciseDao.returnOrderedQuery(query, filterAndOrder, page, idUser)
        )
    }
}