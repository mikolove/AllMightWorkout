package com.mikolove.core.data.repositories.exercise

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkService


class ExerciseSetNetworkDataSourceImpl
constructor(
    private val exerciseSetNetworkService : ExerciseSetNetworkService
) : ExerciseSetNetworkDataSource {

    override suspend fun upsertExerciseSet(
        exerciseSet: ExerciseSet,
        idExercise: String,
        idWorkout: String
    ) = exerciseSetNetworkService.upsertExerciseSet(exerciseSet,idExercise,idWorkout)

    override suspend fun removeExerciseSetById(
        primaryKey: String,
        idExercise: String,
        idWorkout: String
    ) = exerciseSetNetworkService.removeExerciseSetById(primaryKey,idExercise,idWorkout)
}
