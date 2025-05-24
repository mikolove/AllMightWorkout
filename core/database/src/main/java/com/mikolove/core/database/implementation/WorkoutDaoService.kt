package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.ExerciseSetDao
import com.mikolove.core.database.database.WorkoutDao
import com.mikolove.core.database.database.WorkoutGroupDao
import com.mikolove.core.database.mappers.toExerciseSetCacheEntity
import com.mikolove.core.database.mappers.toWorkout
import com.mikolove.core.database.mappers.toWorkoutCacheEntity
import com.mikolove.core.database.model.WorkoutGroupCacheEntity
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime

class WorkoutDaoService(
    private val workoutDao: WorkoutDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val workoutGroupDao: WorkoutGroupDao
) : WorkoutCacheService {

    override suspend fun upsertWorkout(workout: Workout, idUser : String): Long {
        return workoutDao.upsertWorkout(workout.toWorkoutCacheEntity(idUser))
    }

    override suspend fun upsertWorkouts(workouts: List<Workout>, idUser: String): List<Long> {
        return workoutDao.upsertWorkouts(workouts.map { it.toWorkoutCacheEntity(idUser) })
    }

    override suspend fun removeWorkouts(workoutIds :  List<String>): Int {
        return workoutDao.removeWorkouts(workoutIds)
    }

    override suspend fun removeWorkout(workoutId: String): Int {
        return workoutDao.removeWorkout(workoutId)
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout {
        return workoutDao.getWorkoutById(primaryKey).toWorkout()
    }

    override  fun getWorkouts(idUser: String,searchQuery: String): Flow<List<Workout>> {
        return workoutDao.getWorkouts(idUser,searchQuery)
            .map { entities ->
                entities.map { it.toWorkout() }
            }
    }

    override  fun getWorkoutByWorkoutType(idWorkoutType: List<String>, idUser: String): Flow<List<Workout>> {
        return workoutDao.getWorkoutWithExercises(idWorkoutType, idUser)
            .map { entities ->
                entities.map {
                    it.toWorkout()
                }
            }
    }

    override  fun getWorkoutByWorkoutTypeByGroup(
        idWorkoutType: List<String>,
        idGroup: List<String>,
        idUser: String
    ): Flow<List<Workout>> {
        return workoutDao.getWorkoutWithExercisesWithGroups(idWorkoutType,idGroup,idUser)
            .map { entities ->
            entities.map {
                it.toWorkout()
            }
        }
    }

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Boolean {
        return exerciseSetDao.isExerciseInWorkout(idWorkout,idExercise) > 0
    }

    override suspend fun addExerciseToWorkout(workoutId: String, exerciseId: String, set: ExerciseSet): Long {
        return exerciseSetDao.addExerciseSet(set.toExerciseSetCacheEntity(workoutId,exerciseId))
    }

    override suspend fun removeExerciseFromWorkout(workoutId: String, exerciseId: String, sets: List<ExerciseSet>): Int {
        val ids = sets.mapIndexed { _, exerciseSet -> exerciseSet.idExerciseSet }
        return exerciseSetDao.removeExerciseSets(ids,workoutId,exerciseId)
    }

    override suspend fun isWorkoutInGroup(idWorkout: String, idGroup: String): Boolean {
        return workoutGroupDao.isWorkoutInGroup(idWorkout,idGroup) >0
    }

    override suspend fun addWorkoutToGroup(workoutId: String, groupId: String): Long {
        return workoutGroupDao.addWorkoutToGroup(
            WorkoutGroupCacheEntity(
                idWorkout = workoutId,
                idGroup = groupId,
                createdAt = ZonedDateTime.now()
            )
        )
    }

    override suspend fun removeWorkoutFromGroup(workoutId: String, groupId: String): Int {
        return workoutGroupDao.removeWorkoutFromGroup(workoutId,groupId)
    }
}