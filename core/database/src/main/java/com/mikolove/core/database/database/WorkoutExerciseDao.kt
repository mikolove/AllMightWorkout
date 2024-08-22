package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutExerciseCacheEntity

@Dao
interface WorkoutExerciseDao {

    @Query("""
        SELECT count(*)
        FROM workouts_exercises
        WHERE id_exercise = :idExercise AND id_workout = :idWorkout
    """)
    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExerciseToWorkout(workoutExerciseEntity : WorkoutExerciseCacheEntity) : Long

    @Query("DELETE FROM workouts_exercises WHERE id_workout =:workoutId AND id_exercise = :exerciseId")
    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String) : Int

}