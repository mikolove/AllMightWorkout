package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mikolove.core.database.model.ExerciseBodyPartCacheEntity

@Dao
interface ExerciseBodyPartDao{

    @Query("""
        SELECT count(*)
        FROM exercises_bodypart
        WHERE id_exercise = :idExercise AND id_body_part = :idBodyPart
    """)
    suspend fun isBodyPartInExercise(idExercise : String, idBodyPart : String) : Int

/*
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBodyPartToExercise(exerciseBodyPartCacheEntity: ExerciseBodyPartCacheEntity) : Long

    @Query("DELETE FROM exercises_bodypart WHERE id_exercise = :idExercise")
    suspend fun removeBodyPartsFromExercise(idExercise: String) : Int
*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun addBodyPartToExercise(exerciseBodyPartCacheEntity: ExerciseBodyPartCacheEntity) : Long

    @Query("DELETE FROM exercises_bodypart WHERE id_exercise = :idExercise")
    abstract fun removeBodyPartsFromExercise(idExercise: String) : Int

    @Transaction
    suspend fun insertBodyPartsAndCleanInTransaction(idExercise: String, bodyPartsCacheEntity : List<ExerciseBodyPartCacheEntity>){
        removeBodyPartsFromExercise(idExercise)
        bodyPartsCacheEntity.forEach{ bp ->
            addBodyPartToExercise(bp)
        }
    }
}