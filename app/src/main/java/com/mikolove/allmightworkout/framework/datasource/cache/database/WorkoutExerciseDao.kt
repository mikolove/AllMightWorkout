package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WorkoutExerciseDao {

    @Query("""
        SELECT count(*)
        FROM workouts_exercises
        WHERE id_exercise = :idExercise AND id_workout = :idWorkout
    """)
    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Int

    @Query("INSERT INTO workouts_exercises(id_workout,id_exercise,created_at) VALUES(:workoutId,:exerciseId, CURRENT_DATE)")
    suspend fun addExerciseToWorkout(workoutId : String, exerciseId : String) : Long

    @Query("DELETE FROM workouts_exercises WHERE id_workout =:workoutId AND id_exercise = :exerciseId")
    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String) : Int

}