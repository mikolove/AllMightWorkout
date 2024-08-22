package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.allmightworkout.framework.datasource.cache.model.BodyPartCacheEntity

const val BODYPART_ORDER_ASC: String = ""
const val BODYPART_ORDER_DESC: String = "-"
const val BODYPART_FILTER_NAME = "name"

const val BODYPART_ORDER_BY_ASC_NAME = BODYPART_ORDER_ASC + BODYPART_FILTER_NAME
const val BODYPART_ORDER_BY_DESC_NAME = BODYPART_ORDER_DESC + BODYPART_FILTER_NAME

const val BODYPART_PAGINATION_PAGE_SIZE = 30

@Dao
interface BodyPartDao{

    @Insert
    suspend fun insertBodyPart(bodyPart: BodyPartCacheEntity) : Long

    @Query("""
        UPDATE body_parts
        SET
        name = :name
        WHERE id_body_part = :idBodyPart
    """)
    suspend fun updateBodyPart(idBodyPart: String, name: String): Int

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

    @Query("""
        SELECT count(*)
        FROM body_parts, workout_types
        WHERE body_parts.fk_id_workout_type = workout_types.id_workout_type
        AND workout_types.id_workout_type = :idWorkoutType
    """)
    suspend fun getTotalBodyPartsByWorkoutType(idWorkoutType: String) : Int

    @Query("""
        SELECT count(*)
        FROM body_parts
    """)
    suspend fun getTotalBodyParts() : Int

    @Query("SELECT * FROM body_parts")
    suspend fun getBodyParts() : List<BodyPartCacheEntity>

    @Query("""
        SELECT *
        FROM body_parts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name DESC LIMIT ( :page* :pageSize)
    """)
    suspend fun getBodyPartOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = BODYPART_PAGINATION_PAGE_SIZE
    ): List<BodyPartCacheEntity>

    @Query("""
        SELECT *
        FROM body_parts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name ASC LIMIT ( :page* :pageSize)
    """)
    suspend fun getBodyPartOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = BODYPART_PAGINATION_PAGE_SIZE
    ): List<BodyPartCacheEntity>

}

suspend fun BodyPartDao.returnOrderedQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<BodyPartCacheEntity>{
    when{
        filterAndOrder.contains(BODYPART_ORDER_BY_ASC_NAME) ->{
            return getBodyPartOrderByNameASC(
                query,
                page
            )
        }
        filterAndOrder.contains(BODYPART_ORDER_BY_DESC_NAME) ->{
            return getBodyPartOrderByNameDESC(
                query,
                page
            )
        }
        else -> return getBodyParts()
    }
}
