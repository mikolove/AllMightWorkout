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

    override suspend fun insertWorkout(workout: Workout): Long {
        return workoutDao.insertWorkout(workoutCacheMapper.mapToEntity(workout))
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
        val ids = workouts.mapIndexed { index, workout -> workout.idWorkout }
        return workoutDao.removeWorkouts(ids)
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout? {
        return workoutDao.getWorkoutById(primaryKey)?.let {
            workoutWithExercisesCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getTotalWorkout(): Int {
        return workoutDao.getTotalWorkout()
    }

    override suspend fun getWorkouts(): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkouts()
        )
    }

    override suspend fun getWorkoutsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByDateDESC(query, page)
        )
    }

    override suspend fun getWorkoutsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByDateASC(query, page)
        )
    }

    override suspend fun getWorkoutsOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByNameDESC(query, page)
        )
    }

    override suspend fun getWorkoutsOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
            workoutDao.getWorkoutsOrderByNameASC(query, page)
        )
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Workout> {
        return workoutWithExercisesCacheMapper.entityListToDomainList(
             workoutDao.returnOrderedQuery(query, filterAndOrder, page)
        )
    }
}