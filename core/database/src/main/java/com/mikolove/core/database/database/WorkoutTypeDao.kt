package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mikolove.core.database.model.WorkoutTypeCacheEntity
import com.mikolove.core.database.model.WorkoutTypeWithBodyPartCacheEntity

@Dao
interface WorkoutTypeDao{

    @Upsert
    suspend fun upsertWorkoutType(workoutTypes: WorkoutTypeCacheEntity) : Long

    @Query("DELETE FROM workout_types WHERE id_workout_type = :primaryKey")
    suspend fun removeWorkoutType(primaryKey: String) : Int

    @Transaction
    @Query("""
        SELECT workout_types.id_workout_type, workout_types.name
        FROM workout_types, body_parts
        WHERE workout_types.id_workout_type = body_parts.fk_id_workout_type
        AND body_parts.id_body_part = :idBodyPart
    """)
    suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String) : WorkoutTypeWithBodyPartCacheEntity

    @Transaction
    @Query("SELECT * FROM workout_types WHERE id_workout_type = :primaryKey")
    suspend fun getWorkoutTypeById(primaryKey: String) : WorkoutTypeWithBodyPartCacheEntity

    @Transaction
    @Query("SELECT * FROM workout_types")
    suspend fun getWorkoutTypes() : List<WorkoutTypeWithBodyPartCacheEntity>

}