package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseWithSetsCacheEntity

@Dao
interface HistoryExerciseDao {

    @Insert
    suspend fun insertHistoryExercise(historyExercise: HistoryExerciseCacheEntity) : Long

    @Query("SELECT * FROM history_exercises WHERE fk_id_history_workout = :idHistoryWorkout")
    suspend fun getHistoryExercisesByHistoryWorkout(idHistoryWorkout: String): List<HistoryExerciseWithSetsCacheEntity>?

    @Query("SELECT * FROM history_exercises WHERE id_history_exercise = :primaryKey")
    suspend fun getHistoryExerciseById(primaryKey : String) : HistoryExerciseWithSetsCacheEntity?

    @Query("SELECT count(*) FROM history_exercises")
    suspend fun getTotalHistoryExercise() : Int

}