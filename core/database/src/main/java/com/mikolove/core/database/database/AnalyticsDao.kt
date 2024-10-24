package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mikolove.core.database.model.HistoryExerciseCacheEntity
import com.mikolove.core.database.model.HistoryExerciseSetCacheEntity
import com.mikolove.core.database.model.HistoryWorkoutCacheEntity
import com.mikolove.core.database.model.HistoryWorkoutWithExercisesCacheEntity

@Dao
interface AnalyticsDao{

    @Insert
    suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkoutCacheEntity) : Long

    @Query("DELETE FROM history_workouts WHERE id_history_workout = :idHistoryWorkout")
    suspend fun deleteHistoryWorkout(idHistoryWorkout : String) : Int

    @Transaction
    @Query("SELECT * FROM history_workouts WHERE id_history_workout = :primaryKey")
    suspend fun getHistoryWorkoutById(primaryKey : String) : HistoryWorkoutWithExercisesCacheEntity

    @Transaction
    @Query("SELECT * FROM history_workouts WHERE fk_id_user = :idUser")
    suspend fun getHistoryWorkouts(idUser: String) : List<HistoryWorkoutWithExercisesCacheEntity>

    @Insert
    suspend fun insertHistoryExercise(historyExercise: HistoryExerciseCacheEntity) : Long

    @Insert
    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSetCacheEntity) : Long
}