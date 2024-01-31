package com.mikolove.allmightworkout.framework.datasource.cache.implementation

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.database.WorkoutDao
import com.mikolove.allmightworkout.framework.datasource.cache.database.returnOrderedQuery
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutCacheMapper
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.WorkoutWithExercisesCacheMapper

class WorkoutDaoServiceImpl
constructor(
    private val workoutDao: WorkoutDao,
    private val workoutCacheMapper : WorkoutCacheMapper,
    private val workoutWithExercisesCacheMapper: WorkoutWithExercisesCacheMapper,
    private val dateUtil: DateUtil
) : WorkoutDaoService{

    override suspend fun insertWorkout(workout: Workout, idUser : String): Long {
        val entity = workoutCacheMapper.mapToEntity(workout).copy(idUser = idUser)
        return workoutDao.insertWorkout(entity)
    }

    override suspend fun updateWorkout(
        primaryKey: String,
        name: String,
        updatedAt: String,
        isActive: Boolean
    ): Int {
        return workoutDao.updateWorkout(
            primaryKey = primaryKey,
            name = name,
            updatedAt = dateUtil.convertStringDateToDate(updatedAt),
            isActive = isActive
        )
    }

    override suspend fun removeWorkout(id: String): Int {
        return workoutDao.removeWorkout(id)
    }

    override suspend fun removeWorkouts(workouts: List<Workout>): Int {
        val ids = workouts.mapIndexed { _, workout -> workout.idWorkout }
        return workoutDao.removeWorkouts(ids)
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {
        return workoutDao.getWorkoutById(primaryKey)?.let {
            workoutWithExercisesCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getExerciseIdsUpdate(idWorkout: String): String? {
        return workoutDao.getExerciseIdsUpdate(idWorkout).let {
            dateUtil.convertDateToStringDate(it)
        }
    }

    override suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?): Int {
        return workoutDao.updateExerciseIdsUpdatedAt(
            idWorkout,
            exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToDate(it) }
        )
    }

    override suspend fun getTotalWorkout(idUser : String): Int {
        return workoutDao.getTotalWorkout(idUser)
    }

    override suspend fun getWorkouts(idUser: String): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkouts(idUser)
        )
    }

    override suspend fun getWorkoutsOrderByDateDESC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByDateDESC(query, page, idUser)
        )
    }

    override suspend fun getWorkoutsOrderByDateASC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByDateASC(query, page, idUser)
        )
    }

    override suspend fun getWorkoutsOrderByNameDESC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByNameDESC(query, page, idUser)
        )
    }

    override suspend fun getWorkoutsOrderByNameASC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByNameASC(query, page, idUser)
        )
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser: String
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
             workoutDao.returnOrderedQuery(query, filterAndOrder, page, idUser)
        )
    }
}