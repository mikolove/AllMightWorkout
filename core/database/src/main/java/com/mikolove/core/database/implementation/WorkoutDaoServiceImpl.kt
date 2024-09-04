package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.WorkoutDao
import com.mikolove.core.database.mappers.WorkoutCacheMapper
import com.mikolove.core.database.mappers.WorkoutWithExercisesCacheMapper
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutDaoServiceImpl
constructor(
    private val workoutDao: WorkoutDao,
    private val workoutCacheMapper : WorkoutCacheMapper,
    private val workoutWithExercisesCacheMapper: WorkoutWithExercisesCacheMapper,
) : WorkoutCacheService {

    override suspend fun upsertWorkout(workout: Workout, idUser : String): Long {
        val entity = workoutCacheMapper.mapToEntity(workout).copy(idUser = idUser)
        return workoutDao.insertWorkout(entity)
    }


    override suspend fun removeWorkouts(workouts: List<Workout>): Int {
        val ids = workouts.mapIndexed { _, workout -> workout.idWorkout }
        return workoutDao.removeWorkouts(ids)
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout {
        return workoutDao.getWorkoutById(primaryKey).let {
            workoutWithExercisesCacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getWorkouts(idUser: String): Flow<List<Workout>> {
        return workoutDao.getWorkouts(idUser)
            .map { entities ->
                entities.map { workoutWithExercisesCacheMapper.mapFromEntity(it) }
            }
    }

    override suspend fun getWorkoutByWorkoutType(idWorkoutType: List<String>, idUser: String): List<Workout> {
        return workoutDao.getWorkoutByWorkoutType(idWorkoutType,idUser).let {
            workoutCacheMapper.entityListToDomainList(it)
        }
    }
}