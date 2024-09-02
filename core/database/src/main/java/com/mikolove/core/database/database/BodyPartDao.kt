package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mikolove.core.database.model.BodyPartCacheEntity

const val BODYPART_PAGINATION_PAGE_SIZE = 30

@Dao
interface BodyPartDao{

    @Upsert
    suspend fun upsertBodyPart(bodyPart: BodyPartCacheEntity) : Long

    @Query("DELETE FROM body_parts WHERE id_body_part = :primaryKey")
    suspend fun removeBodyPart(primaryKey: String) : Int

    @Query("""
        SELECT body_parts.id_body_part, body_parts.name, body_parts.fk_id_workout_type 
        FROM body_parts, workout_types
        WHERE body_parts.fk_id_workout_type = workout_types.id_workout_type
        AND workout_types.id_workout_type = :idWorkoutType
    """)
    suspend fun getBodyPartsByWorkoutType(idWorkoutType: String): List<BodyPartCacheEntity>

    @Query("SELECT * FROM body_parts WHERE id_body_part = :primaryKey")
    suspend fun getBodyPartById(primaryKey: String) : BodyPartCacheEntity?


    @Query("SELECT * FROM body_parts")
    suspend fun getBodyParts() : List<BodyPartCacheEntity>

}
