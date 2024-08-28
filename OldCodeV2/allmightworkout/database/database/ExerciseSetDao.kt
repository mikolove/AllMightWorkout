package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.core.database.model.ExerciseSetCacheEntity
import java.util.Date

@Dao
interface ExerciseSetDao{

    @Insert
    suspend fun insertExerciseSet(exerciseSetCacheEntity: ExerciseSetCacheEntity) : Long

    @Query("""
        UPDATE exercise_sets 
        SET 
        reps = :reps,
        weight = :weight,
        duration = :time,
        rest_time = :restTime,
        position = :order,
        updated_at= :updatedAt
        WHERE id_exercise_set = :primaryKey AND fk_id_exercise = :idExercise
    """)
    suspend fun updateExerciseSet(primaryKey: String, reps: Int, weight: Int, time: Int, restTime: Int, order :Int, updatedAt : Date, idExercise: String) : Int

    @Query("DELETE FROM exercise_sets WHERE id_exercise_set IN (:primaryKeys)")
    suspend fun removeExerciseSets(primaryKeys: List<String>) : Int

    @Query("DELETE FROM exercise_sets WHERE id_exercise_set = :primaryKey AND fk_id_exercise = :idExercise")
    suspend fun removeExerciseSetById(primaryKey :String, idExercise: String) : Int

    @Query("SELECT * FROM exercise_sets WHERE id_exercise_set = :primaryKey AND fk_id_exercise = :idExercise")
    suspend fun getExerciseSetById(primaryKey: String, idExercise: String) : ExerciseSetCacheEntity

    @Query("SELECT * FROM exercise_sets WHERE fk_id_exercise = :idExercise ORDER BY created_at DESC")
    suspend fun getExerciseSetByIdExercise(idExercise : String) : List<ExerciseSetCacheEntity>

    @Query("SELECT count(*) FROM exercise_sets WHERE fk_id_exercise = :idExercise")
    suspend fun getTotalExercisesSetByExercise(idExercise: String) : Int

}