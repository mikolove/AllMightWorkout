package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.ExerciseFirestoreService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExerciseNetworkDateSourceImpl
@Inject
constructor( private val exerciseFirestoreService : ExerciseFirestoreService)
    : ExerciseNetworkDataSource {
    override suspend fun insertExercise(exercise: Exercise)  = exerciseFirestoreService.insertExercise(exercise)

    override suspend fun updateExercise(primaryKey: String, exercise: Exercise) = exerciseFirestoreService.updateExercise(primaryKey, exercise)

    override suspend fun removeExerciseById(primaryKey: String)= exerciseFirestoreService.removeExerciseById(primaryKey)

    override suspend fun addSets(sets: List<ExerciseSet>)= exerciseFirestoreService.addSets(sets)

    override suspend fun removeSets(sets: List<ExerciseSet>) = exerciseFirestoreService.removeSets(sets)

    override suspend fun getExercises(): List<Exercise> = exerciseFirestoreService.getExercises()

    override suspend fun getExerciseById(primaryKey: String): Exercise? = exerciseFirestoreService.getExerciseById(primaryKey)

}
