package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mikolove.core.database.model.HistoryExerciseCacheEntity
import com.mikolove.core.database.model.HistoryExerciseWithSetsCacheEntity

@Dao
interface HistoryExerciseDao {

    @Insert
    suspend fun insertHistoryExercise(historyExercise: HistoryExerciseCacheEntity) : Long

    @Transaction
    @Query("SELECT * FROM history_exercises WHERE fk_id_history_workout = :idHistoryWorkout")
    suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExerciseWithSetsCacheEntity>?

    @Transaction
    @Query("SELECT * FROM history_exercises WHERE id_history_exercise = :primaryKey")
    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExerciseWithSetsCacheEntity?

    @Query("SELECT count(*) FROM history_exercises")
    suspend fun getTotalHistoryExercise() : Int

}