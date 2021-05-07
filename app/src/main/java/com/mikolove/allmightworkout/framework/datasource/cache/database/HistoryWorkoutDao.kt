package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryWorkoutCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryWorkoutWithExercisesCacheEntity

const val HISTORY_WORKOUT_ORDER_ASC: String = ""
const val HISTORY_WORKOUT_ORDER_DESC: String = "-"
const val HISTORY_WORKOUT_FILTER_NAME = "name"
const val HISTORY_WORKOUT_FILTER_DATE_CREATED = "createdAt"

const val HISTORY_WORKOUT_ORDER_BY_ASC_DATE_CREATED = HISTORY_WORKOUT_ORDER_ASC + HISTORY_WORKOUT_FILTER_DATE_CREATED
const val HISTORY_WORKOUT_ORDER_BY_DESC_DATE_CREATED = HISTORY_WORKOUT_ORDER_DESC + HISTORY_WORKOUT_FILTER_DATE_CREATED
const val HISTORY_WORKOUT_ORDER_BY_ASC_NAME = HISTORY_WORKOUT_ORDER_ASC + HISTORY_WORKOUT_FILTER_NAME
const val HISTORY_WORKOUT_ORDER_BY_DESC_NAME = HISTORY_WORKOUT_ORDER_DESC + HISTORY_WORKOUT_FILTER_NAME

const val HISTORY_WORKOUT_PAGINATION_PAGE_SIZE = 30

@Dao
interface HistoryWorkoutDao{

    @Insert
    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkoutCacheEntity) : Long

    @Query("""
        SELECT * 
        FROM history_workouts
        WHERE id_history_workout = :primaryKey
    """)
    suspend fun getHistoryWorkoutById(primaryKey : String) : HistoryWorkoutWithExercisesCacheEntity?

    @Query("DELETE FROM history_workouts WHERE id_history_workout = :idHistoryWorkout")
    suspend fun deleteHistoryWorkout(idHistoryWorkout : String) : Int

    @Query("SELECT count(*) FROM history_workouts")
    suspend fun getTotalHistoryWorkout() : Int

    @Query("SELECT * FROM history_workouts")
    suspend fun getHistoryWorkouts() : List<HistoryWorkoutWithExercisesCacheEntity>

    @Query("""
        SELECT * 
        FROM history_workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY created_at DESC LIMIT (:page * :pageSize)
    """)
    suspend fun getHistoryWorkoutOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_WORKOUT_PAGINATION_PAGE_SIZE
    ): List<HistoryWorkoutWithExercisesCacheEntity>

    @Query("""
        SELECT * 
        FROM history_workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY created_at ASC LIMIT (:page * :pageSize)
    """)
    suspend fun getHistoryWorkoutOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_WORKOUT_PAGINATION_PAGE_SIZE
    ): List<HistoryWorkoutWithExercisesCacheEntity>

    @Query("""
        SELECT * 
        FROM history_workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name DESC LIMIT (:page * :pageSize)
    """)
    suspend fun getHistoryWorkoutOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_WORKOUT_PAGINATION_PAGE_SIZE
    ): List<HistoryWorkoutWithExercisesCacheEntity>

    @Query("""
        SELECT * 
        FROM history_workouts
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name ASC LIMIT (:page * :pageSize)
    """)
    suspend fun getHistoryWorkoutOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = HISTORY_WORKOUT_PAGINATION_PAGE_SIZE
    ): List<HistoryWorkoutWithExercisesCacheEntity>


}

suspend fun HistoryWorkoutDao.returnOrderedQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<HistoryWorkoutWithExercisesCacheEntity>{

    when{
        filterAndOrder.contains(HISTORY_WORKOUT_ORDER_BY_DESC_DATE_CREATED) -> {
            return getHistoryWorkoutOrderByDateDESC(query,page)
        }
        filterAndOrder.contains(HISTORY_WORKOUT_ORDER_BY_ASC_DATE_CREATED) -> {
            return getHistoryWorkoutOrderByDateASC(query,page)
        }
        filterAndOrder.contains(HISTORY_WORKOUT_ORDER_BY_DESC_NAME) -> {
            return getHistoryWorkoutOrderByNameDESC(query,page)
        }
        filterAndOrder.contains(HISTORY_WORKOUT_ORDER_BY_ASC_NAME) -> {
            return getHistoryWorkoutOrderByNameASC(query,page)
        }
        else -> return getHistoryWorkouts()
    }
}