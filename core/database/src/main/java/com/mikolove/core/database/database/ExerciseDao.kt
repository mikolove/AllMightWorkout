package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mikolove.core.database.model.ExerciseCacheEntity
import com.mikolove.core.database.model.ExerciseWithBodyPartsCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao{

    @Upsert
    suspend fun upsertExercise(exercise: ExerciseCacheEntity) : Long

    @Upsert
    suspend fun upsertExercises(exercise: List<ExerciseCacheEntity>) : List<Long>

    @Query("DELETE FROM exercises WHERE id_exercise = :primaryKey")
    suspend fun removeExercise(primaryKey: String)  : Int

    @Query("DELETE FROM exercises WHERE id_exercise IN (:primaryKeys)")
    suspend fun removeExercises(primaryKeys: List<String>)  : Int

    @Transaction
    @Query("SELECT * FROM exercises WHERE fk_id_user = :idUser")
    fun getExercises(idUser : String) : Flow<List<ExerciseWithBodyPartsCacheEntity>>

    @Transaction
    @Query("SELECT e.id_exercise,e.name,e.exercise_type,e.is_active,e.fk_id_user,e.created_at,e.updated_at,bp.id_body_part,bp.name,bp.fk_id_workout_type " +
            "FROM exercises AS e, exercises_bodypart , body_parts AS bp , workout_types " +
            "WHERE e.id_exercise = exercises_bodypart.id_exercise " +
            "AND exercises_bodypart.id_body_part = bp.id_body_part " +
            "AND bp.fk_id_workout_type = workout_types.id_workout_type " +
            "AND workout_types.id_workout_type IN (:workoutTypes)" +
            "AND e.fk_id_user = :idUser " +
            "GROUP BY e.id_exercise")
    fun getExercisesByWorkoutTypes(workoutTypes : List<String>,idUser : String) : Flow<List<ExerciseWithBodyPartsCacheEntity>>

    @Transaction
    @Query("SELECT * FROM exercises WHERE id_exercise = :primaryKey")
    suspend fun getExerciseById(primaryKey: String) : ExerciseWithBodyPartsCacheEntity
}