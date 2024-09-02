package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikolove.core.database.model.ExerciseSetCacheEntity

@Dao
interface ExerciseSetDao {

    @Query("""
        SELECT count(*)
        FROM exercises_sets
        WHERE id_exercise = :idExercise AND id_workout = :idWorkout
    """)
    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExerciseToWorkout(workoutExerciseEntity : ExerciseSetCacheEntity) : Long

    @Query("DELETE FROM exercises_sets WHERE id_workout =:workoutId AND id_exercise = :exerciseId")
    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String) : Int

}