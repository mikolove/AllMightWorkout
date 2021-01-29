package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseDaoService
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutExerciseDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseCacheDataSourceImpl
@Inject
constructor(
    private val exerciseDaoService : ExerciseDaoService,
    private val workoutExerciseDaoService : WorkoutExerciseDaoService)
    : ExerciseCacheDataSource {

    override suspend fun insertExercise(exercise: Exercise): Long = exerciseDaoService.insertExercise(exercise)

    override suspend fun updateExercise(primaryKey: String, name: String, bodyPart: BodyPart?, isActive: Boolean, exerciseType: String): Int =
        exerciseDaoService.updateExercise(primaryKey,name,bodyPart,isActive,exerciseType)

    override suspend fun removeExerciseById(primaryKey: String): Int = exerciseDaoService.removeExerciseById(primaryKey)

    override suspend fun getExercises(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Exercise> {
        return exerciseDaoService.returnOrderedQuery(query,filterAndOrder,page)
    }

    override suspend fun getAllExercises(): List<Exercise> = exerciseDaoService.getAllExercises()

    override suspend fun removeExercises(exercises: List<Exercise>): Int = exerciseDaoService.removeExercises(exercises)

    override suspend fun getExerciseById(primaryKey: String): Exercise? = exerciseDaoService.getExerciseById(primaryKey)

    override suspend fun getTotalExercises(): Int = exerciseDaoService.getTotalExercises()

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? = exerciseDaoService.getExercisesByWorkout(idWorkout)

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String): Long = workoutExerciseDaoService.addExerciseToWorkout(idWorkout, idExercise)

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String): Int = workoutExerciseDaoService.removeExerciseFromWorkout(idWorkout, idExercise)

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int = workoutExerciseDaoService.isExerciseInWorkout(idWorkout,idExercise)
}
