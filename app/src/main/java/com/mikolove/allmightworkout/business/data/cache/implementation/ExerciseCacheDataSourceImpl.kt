package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutExerciseDaoService

class ExerciseCacheDataSourceImpl
constructor(
    private val exerciseDaoService : ExerciseDaoService,
    private val workoutExerciseDaoService : WorkoutExerciseDaoService)
    : ExerciseCacheDataSource {

    override suspend fun insertExercise(exercise: Exercise, idUser: String): Long = exerciseDaoService.insertExercise(exercise, idUser)

    override suspend fun updateExercise(
        primaryKey: String,
        name: String,
        bodyPart: BodyPart?,
        isActive: Boolean,
        exerciseType: String,
        updatedAt: String
    ): Int =
        exerciseDaoService.updateExercise(primaryKey, name, bodyPart, isActive, exerciseType,updatedAt)

    override suspend fun removeExerciseById(primaryKey: String): Int = exerciseDaoService.removeExerciseById(primaryKey)

    override suspend fun getExercises(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser : String
    ): List<Exercise> {
        return exerciseDaoService.returnOrderedQuery(query,filterAndOrder,page,idUser)
    }

    override suspend fun removeExercises(exercises: List<Exercise>): Int = exerciseDaoService.removeExercises(exercises)

    override suspend fun getExerciseById(primaryKey: String): Exercise? = exerciseDaoService.getExerciseById(primaryKey)

    override suspend fun getTotalExercises(idUser : String): Int = exerciseDaoService.getTotalExercises(idUser)

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? = exerciseDaoService.getExercisesByWorkout(idWorkout)

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String): Long = workoutExerciseDaoService.addExerciseToWorkout(idWorkout, idExercise)

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String): Int = workoutExerciseDaoService.removeExerciseFromWorkout(idWorkout, idExercise)

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int = workoutExerciseDaoService.isExerciseInWorkout(idWorkout,idExercise)
}
