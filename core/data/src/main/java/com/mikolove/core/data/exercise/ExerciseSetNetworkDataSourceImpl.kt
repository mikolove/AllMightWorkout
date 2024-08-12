package com.mikolove.core.data.exercise

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseSetFirestoreService


class ExerciseSetNetworkDataSourceImpl
constructor(
    private val exerciseSetFirestoreService : ExerciseSetFirestoreService
) : ExerciseSetNetworkDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise : String ) = exerciseSetFirestoreService.insertExerciseSet(exerciseSet,idExercise)

    override suspend fun updateExerciseSet(exerciseSet: ExerciseSet, idExercise: String) = exerciseSetFirestoreService.updateExerciseSet(
        exerciseSet,
        idExercise
    )

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise : String)  = exerciseSetFirestoreService.removeExerciseSetById(primaryKey,idExercise)

    override suspend fun removeExerciseSetsById(primaryKeys: List<String>, idExercise: String) = exerciseSetFirestoreService.removeExerciseSetsById( primaryKeys, idExercise)

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? = exerciseSetFirestoreService.getExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet>? = exerciseSetFirestoreService.getExerciseSetByIdExercise(idExercise)

    override suspend fun getTotalExercisesSetByExercise(idExercise: String): Int = exerciseSetFirestoreService.getTotalExercisesSetByExercise(idExercise)

    //TODO : Delete this at a moment firestore structure changed
    override suspend fun insertDeletedExerciseSet(exerciseSet: ExerciseSet) = exerciseSetFirestoreService.insertDeletedExerciseSet(exerciseSet)
    //TODO : Delete this at a moment firestore structure changed
    override suspend fun getDeletedExerciseSets(): List<ExerciseSet> = exerciseSetFirestoreService.getDeletedExerciseSets()

    override suspend fun updateExerciseSets(exerciseSets: List<ExerciseSet>, idExercise: String) = exerciseSetFirestoreService.updateExerciseSets(exerciseSets,idExercise)

    override suspend fun insertDeletedExerciseSets(exerciseSets: List<ExerciseSet>) = exerciseSetFirestoreService.insertDeletedExerciseSets(exerciseSets)

}
