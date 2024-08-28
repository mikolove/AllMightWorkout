package com.mikolove.core.data.repositories.exercise

import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkService
import com.mikolove.core.domain.exercise.ExerciseSet


class ExerciseSetNetworkDataSourceImpl
constructor(
    private val exerciseSetNetworkService : ExerciseSetNetworkService
) : ExerciseSetNetworkDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise : String ) = exerciseSetNetworkService.insertExerciseSet(exerciseSet,idExercise)

    override suspend fun updateExerciseSet(exerciseSet: ExerciseSet, idExercise: String) = exerciseSetNetworkService.updateExerciseSet(
        exerciseSet,
        idExercise
    )

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise : String)  = exerciseSetNetworkService.removeExerciseSetById(primaryKey,idExercise)

    override suspend fun removeExerciseSetsById(primaryKeys: List<String>, idExercise: String) = exerciseSetNetworkService.removeExerciseSetsById( primaryKeys, idExercise)

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? = exerciseSetNetworkService.getExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet>? = exerciseSetNetworkService.getExerciseSetByIdExercise(idExercise)

    override suspend fun getTotalExercisesSetByExercise(idExercise: String): Int = exerciseSetNetworkService.getTotalExercisesSetByExercise(idExercise)

    //TODO : Delete this at a moment firestore structure changed
    override suspend fun insertDeletedExerciseSet(exerciseSet: ExerciseSet) = exerciseSetNetworkService.insertDeletedExerciseSet(exerciseSet)
    //TODO : Delete this at a moment firestore structure changed
    override suspend fun getDeletedExerciseSets(): List<ExerciseSet> = exerciseSetNetworkService.getDeletedExerciseSets()

    override suspend fun updateExerciseSets(exerciseSets: List<ExerciseSet>, idExercise: String) = exerciseSetNetworkService.updateExerciseSets(exerciseSets,idExercise)

    override suspend fun insertDeletedExerciseSets(exerciseSets: List<ExerciseSet>) = exerciseSetNetworkService.insertDeletedExerciseSets(exerciseSets)

}
