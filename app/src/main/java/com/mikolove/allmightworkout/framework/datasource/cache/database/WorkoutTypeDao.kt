package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutTypeCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutTypeWithBodyPartCacheEntity

const val WORKOUTTYPE_ORDER_ASC: String = ""
const val WORKOUTTYPE_ORDER_DESC: String = "-"
const val WORKOUTTYPE_FILTER_NAME = "name"

const val WORKOUTTYPE_ORDER_BY_ASC_NAME = WORKOUTTYPE_ORDER_ASC + WORKOUTTYPE_FILTER_NAME
const val WORKOUTTYPE_ORDER_BY_DESC_NAME = WORKOUTTYPE_ORDER_DESC + WORKOUTTYPE_FILTER_NAME

const val WORKOUTTYPE_PAGINATION_PAGE_SIZE = 30

@Dao
interface WorkoutTypeDao{

    @Insert
    suspend fun insertWorkoutType(workoutTypes: WorkoutTypeCacheEntity) : Long

    @Query("""
        UPDATE workout_types
        SET name = :name
        WHERE id_workout_type = :idWorkoutType
    """)
    suspend fun updateWorkoutType(idWorkoutType: String, name: String): Int

    @Query("DELETE FROM workout_types WHERE id_workout_type = :primaryKey")
    suspend fun removeWorkoutType(primaryKey: String) : Int

    @Query("""
        SELECT workout_types.id_workout_type, workout_types.name
        FROM workout_types, body_parts
        WHERE workout_types.id_workout_type = body_parts.fk_id_workout_type
        AND body_parts.id_body_part = :idBodyPart
    """)
    suspend fun getWorkoutTypeBydBodyPartId(idBodyPart: String?) : WorkoutTypeWithBodyPartCacheEntity?

    @Query("SELECT * FROM workout_types WHERE id_workout_type = :primaryKey")
    suspend fun getWorkoutTypeById(primaryKey: String) : WorkoutTypeWithBodyPartCacheEntity?

    @Query("SELECT * FROM workout_types")
    suspend fun getWorkoutTypes() : List<WorkoutTypeWithBodyPartCacheEntity>

    @Query("SELECT count(*) FROM workout_types")
    suspend fun getTotalWorkoutTypes() : Int

    @Query("""
        SELECT *
        FROM workout_types
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name DESC LIMIT (:page*:pageSize)
    """)
    suspend fun getWorkoutTypeOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUTTYPE_PAGINATION_PAGE_SIZE
    ): List<WorkoutTypeWithBodyPartCacheEntity>

    @Query("""
        SELECT *
        FROM workout_types
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name ASC LIMIT (:page*:pageSize)
    """)
    suspend fun getWorkoutTypeOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUTTYPE_PAGINATION_PAGE_SIZE
    ): List<WorkoutTypeWithBodyPartCacheEntity>

}

suspend fun WorkoutTypeDao.returnOrderedQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<WorkoutTypeWithBodyPartCacheEntity>{
    when{
        filterAndOrder.contains(WORKOUTTYPE_ORDER_BY_ASC_NAME) -> {
            return getWorkoutTypeOrderByNameASC(
                query,
                page
            )
        }
        filterAndOrder.contains(WORKOUTTYPE_ORDER_BY_DESC_NAME) -> {
            return getWorkoutTypeOrderByNameDESC(
                query,
                page
            )
        }
        else -> return getWorkoutTypes()
    }
}