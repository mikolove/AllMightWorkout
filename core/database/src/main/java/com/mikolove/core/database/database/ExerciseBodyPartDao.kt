package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikolove.core.database.model.ExerciseBodyPartCacheEntity

@Dao
interface ExerciseBodyPartDao{

    @Query("""
        SELECT count(*)
        FROM exercises_bodypart
        WHERE id_exercise = :idExercise AND id_body_part = :idBodyPart
    """)
    suspend fun isBodyPartInExercise(idExercise : String, idBodyPart : String) : Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBodyPartToExercise(exerciseBodyPartCacheEntity: ExerciseBodyPartCacheEntity) : Long

    @Query("DELETE FROM exercises_bodypart WHERE id_exercise = :idExercise AND id_body_part = :idBodyPart")
    suspend fun removeBodyPartFromExercise(idExercise: String,idBodyPart: String)
}