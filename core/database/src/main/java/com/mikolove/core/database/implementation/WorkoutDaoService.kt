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
        return workoutDao.insertWorkout(workout.toWorkoutCacheEntity(idUser))
    }

    override suspend fun removeWorkouts(workouts: List<Workout>): Int {
        val ids = workouts.mapIndexed { _, workout -> workout.idWorkout }
        return workoutDao.removeWorkouts(ids)
    }

    override suspend fun getWorkoutById(primaryKey: String): Workout {
        return workoutDao.getWorkoutById(primaryKey).toWorkout()
    }

    override  fun getWorkouts(idUser: String): Flow<List<Workout>> {
        return workoutDao.getWorkouts(idUser)
            .map { entities ->
                entities.map { it.toWorkout() }
            }
    }

    override suspend fun getWorkoutByWorkoutType(idWorkoutType: List<String>, idUser: String): List<Workout> {
        return workoutDao.getWorkoutByWorkoutType(idWorkoutType,idUser).map { it.toWorkout() }
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