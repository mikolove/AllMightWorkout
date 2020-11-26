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

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet) = exerciseSetFirestoreService.insertExerciseSet(exerciseSet)

    override suspend fun updateExerciseSet(primaryKey: String, exerciseSet: ExerciseSet) = exerciseSetFirestoreService.updateExerciseSet(primaryKey, exerciseSet)

    override suspend fun removeExerciseSetById(primaryKey: String)  = exerciseSetFirestoreService.removeExerciseSetById(primaryKey)

    override suspend fun getExerciseSetByExerciseId(primaryKey: String): List<ExerciseSet> = exerciseSetFirestoreService.getExerciseSetByExerciseId(primaryKey)
}
