package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseSetFirestoreService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExerciseSetNetworkDataSourceImpl
@Inject
constructor(private val exerciseSetFirestoreService : ExerciseSetFirestoreService) :
    ExerciseSetNetworkDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise : String ) = exerciseSetFirestoreService.insertExerciseSet(exerciseSet,idExercise)

    override suspend fun updateExerciseSet(primaryKey: String, exerciseSet: ExerciseSet, idExercise : String) = exerciseSetFirestoreService.updateExerciseSet(primaryKey, exerciseSet,idExercise)

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise : String)  = exerciseSetFirestoreService.removeExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? = exerciseSetFirestoreService.getExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet>? = exerciseSetFirestoreService.getExerciseSetByIdExercise(idExercise)
}
