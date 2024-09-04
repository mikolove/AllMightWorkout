package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.core.database.model.WorkoutCacheEntity
import com.mikolove.core.database.model.WorkoutWithExercisesCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insertWorkout(workout : WorkoutCacheEntity) : Long

    @Query("DELETE FROM workouts WHERE id_workout IN (:ids)")
    suspend fun removeWorkouts(ids : List<String>) : Int

    @Query("SELECT * FROM workouts WHERE id_workout = :primaryKey")
    suspend fun getWorkoutById(primaryKey: String) : WorkoutWithExercisesCacheEntity

    @Query("SELECT * FROM workouts WHERE fk_id_user = :idUser")
    suspend fun getWorkouts(idUser: String) : Flow<List<WorkoutWithExercisesCacheEntity>>

    @Query("""SELECT w.id_workout, w.name, w.is_active, w.fk_id_user, w.created_at, w.updated_at
        FROM workouts w, exercises_sets ex, exercises e, body_parts bp, workout_types wt
        WHERE w.id_workout = ex.id_workout
         AND ex.id_exercise = e.id_exercise
         AND e.fk_id_body_part = bp.id_body_part
         AND bp.fk_id_workout_type IN (:ids)
         AND w.fk_id_user=:idUser""")
    suspend fun getWorkoutByWorkoutType(ids : List<String>,idUser: String) : List<WorkoutCacheEntity>
}