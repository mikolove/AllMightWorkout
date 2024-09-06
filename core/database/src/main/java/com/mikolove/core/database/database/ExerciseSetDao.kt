package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mikolove.core.database.model.ExerciseSetCacheEntity

@Dao
interface ExerciseSetDao {

    @Query("SELECT COUNT(*) FROM exercises_sets WHERE id_workout = :idWorkout AND id_exercise = :idExercise" )
    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExerciseSet(workoutExerciseEntity : ExerciseSetCacheEntity) : Long

    @Query("DELETE FROM exercises_sets WHERE id_workout =:workoutId AND id_exercise = :exerciseId AND id_exercise_set IN (:ids)")
    suspend fun removeExerciseSets(ids : List<String>,workoutId : String, exerciseId : String) : Int

}