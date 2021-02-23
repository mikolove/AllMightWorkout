package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutWithExercisesCacheEntity
import java.util.*

const val WORKOUT_ORDER_ASC: String = ""
const val WORKOUT_ORDER_DESC: String = "-"
const val WORKOUT_FILTER_NAME = "name"
const val WORKOUT_FILTER_DATE_CREATED = "created_at"

const val WORKOUT_ORDER_BY_ASC_DATE_CREATED = WORKOUT_ORDER_ASC + WORKOUT_FILTER_DATE_CREATED
const val WORKOUT_ORDER_BY_DESC_DATE_CREATED = WORKOUT_ORDER_DESC + WORKOUT_FILTER_DATE_CREATED
const val WORKOUT_ORDER_BY_ASC_NAME = WORKOUT_ORDER_ASC + WORKOUT_FILTER_NAME
const val WORKOUT_ORDER_BY_DESC_NAME = WORKOUT_ORDER_DESC + WORKOUT_FILTER_NAME

const val WORKOUT_PAGINATION_PAGE_SIZE = 30

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insertWorkout(workout : WorkoutCacheEntity) : Long

    @Query("""
        UPDATE workouts 
        SET 
        name = :name,
        is_active = :isActive,
        updated_at= :updatedAt
        WHERE id_workout = :primaryKey
    """)
    suspend fun updateWorkout(primaryKey: String, name : String, isActive : Boolean, updatedAt : Date) : Int

    @Query("DELETE FROM workouts WHERE id_workout = :id")
    suspend fun removeWorkout(id : String): Int

    @Query("DELETE FROM workouts WHERE id_workout IN (:ids)")
    suspend fun removeWorkouts(ids : List<String>) : Int

    @Query("SELECT * FROM workouts WHERE id_workout = :primaryKey")
    suspend fun getWorkoutById(primaryKey: String) : WorkoutWithExercisesCacheEntity?

    @Query("SELECT count(*) FROM workouts")
    suspend fun getTotalWorkout() : Int

    @Query("SELECT * FROM workouts")
    suspend fun getWorkouts() : List<WorkoutWithExercisesCacheEntity>


    @Query("SELECT exercise_ids_updated_at FROM workouts WHERE id_workout = :idWorkout")
    suspend fun getExerciseIdsUpdate(idWorkout : String) : Date

    @Query("UPDATE workouts SET exercise_ids_updated_at = :exerciseIdsUpdatedAt WHERE id_workout = :idWorkout")
    suspend fun updateExerciseIdsUpdatedAt(idWorkout : String, exerciseIdsUpdatedAt: Date?) : Int


    @Query("""
        SELECT * FROM workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY created_at DESC LIMIT ( :page * :pageSize)
    """)
    suspend fun getWorkoutsOrderByDateDESC( query: String, page : Int, pageSize : Int = WORKOUT_PAGINATION_PAGE_SIZE) : List<WorkoutWithExercisesCacheEntity>

    @Query("""
        SELECT * FROM workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY created_at ASC LIMIT ( :page * :pageSize)
    """)
    suspend fun getWorkoutsOrderByDateASC( query: String, page : Int, pageSize : Int = WORKOUT_PAGINATION_PAGE_SIZE) : List<WorkoutWithExercisesCacheEntity>

    @Query("""
        SELECT * FROM workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name COLLATE NOCASE DESC LIMIT ( :page * :pageSize)
    """)
    suspend fun getWorkoutsOrderByNameDESC( query: String, page : Int, pageSize : Int = WORKOUT_PAGINATION_PAGE_SIZE) : List<WorkoutWithExercisesCacheEntity>

    @Query("""
        SELECT * FROM workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name COLLATE NOCASE ASC LIMIT ( :page * :pageSize)
    """)
    suspend fun getWorkoutsOrderByNameASC( query: String, page : Int, pageSize : Int = WORKOUT_PAGINATION_PAGE_SIZE) : List<WorkoutWithExercisesCacheEntity>

}

suspend fun WorkoutDao.returnOrderedQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<WorkoutWithExercisesCacheEntity>{
    when{
        filterAndOrder.contains(WORKOUT_ORDER_BY_DESC_DATE_CREATED) -> {
            return getWorkoutsOrderByDateDESC(
                query = query,
                page = page
            )
        }
        filterAndOrder.contains(WORKOUT_ORDER_BY_ASC_DATE_CREATED) -> {
            return getWorkoutsOrderByDateASC(
                query = query,
                page = page
            )
        }
        filterAndOrder.contains(WORKOUT_ORDER_BY_DESC_NAME) -> {
            return getWorkoutsOrderByNameDESC(
                query = query,
                page = page
            )
        }
        filterAndOrder.contains(WORKOUT_ORDER_BY_ASC_NAME) -> {
            return getWorkoutsOrderByNameASC(
                query = query,
                page = page
            )
        }
        else -> return getWorkoutsOrderByDateDESC(
            query = query,
            page = page
        )
    }
}