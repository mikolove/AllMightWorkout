package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.ExerciseBodyPartDao
import com.mikolove.core.database.database.ExerciseDao
import com.mikolove.core.database.mappers.toBodyPartCacheEntity
import com.mikolove.core.database.mappers.toExercise
import com.mikolove.core.database.mappers.toExerciseCacheEntity
import com.mikolove.core.database.model.ExerciseBodyPartCacheEntity
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExerciseDaoService(
    private val exerciseDao : ExerciseDao,
    private val exerciseBodyPartDao : ExerciseBodyPartDao
) : ExerciseCacheService {

    override suspend fun upsertExercise(exercise: Exercise, idUser: String): Long {
        return exerciseDao.upsertExercise(exercise.toExerciseCacheEntity(idUser))
    }

    override suspend fun upsertExercises(exercises: List<Exercise>, idUser: String): List<Long> {
        return exerciseDao.upsertExercises(exercises.map { it.toExerciseCacheEntity(idUser) })
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise {
        return exerciseDao.getExerciseById(primaryKey).toExercise()
    }

    override suspend fun removeExercises(exerciseIds : List<String>): Int {
        //val ids = exercises.mapIndexed { _, exercise -> exercise.idExercise }
        return exerciseDao.removeExercises(exerciseIds)
    }

    override suspend fun removeExercise(exerciseId: String): Int {
        return exerciseDao.removeExercise(exerciseId)
    }

    override fun getExercises(idUser: String): Flow<List<Exercise>> {
        return exerciseDao.getExercises(idUser).map{ entities ->
            entities.map{ it.toExercise()}
        }
    }

    override suspend fun isBodyPartInExercise(idExercise: String, idBodyPart: String): Boolean {
        return exerciseBodyPartDao.isBodyPartInExercise(idExercise, idBodyPart) > 0
    }

/*    override suspend fun addBodyPartToExercise(idExercise: String, idBodyPart: String): Long {
        return  exerciseBodyPartDao.addBodyPartToExercise(
            ExerciseBodyPartCacheEntity(
                idExercise = idExercise,
                idBodyPart = idBodyPart,
            )
        )
    }

    override suspend fun removeBodyPartsFromExercise(idExercise: String): Int {
        return  exerciseBodyPartDao.removeBodyPartsFromExercise(idExercise)
    }*/

    override suspend fun insertBodyPartsAndClean(idExercise: String, bodyParts: List<BodyPart>) {
        exerciseBodyPartDao.insertBodyPartsAndCleanInTransaction(
            idExercise,
            bodyParts.map {
                ExerciseBodyPartCacheEntity(
                    idExercise = idExercise,
                    idBodyPart = it.idBodyPart
                )
            }
        )
    }

}