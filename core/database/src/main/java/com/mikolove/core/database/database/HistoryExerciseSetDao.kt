package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.allmightworkout.framework.datasource.cache.model.HistoryExerciseSetCacheEntity

@Dao
interface HistoryExerciseSetDao{

    @Insert
    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSetCacheEntity) : Long

    @Query("SELECT * FROM history_exercise_sets WHERE id_history_exercise_set = :idHistoryExerciseSet")
    suspend fun getHistoryExerciseSetById(idHistoryExerciseSet : String) : HistoryExerciseSetCacheEntity?

    @Query("SELECT * FROM history_exercise_sets WHERE fk_id_history_exercise = :idHistoryExercise")
    suspend fun getHistoryExerciseSetsByHistoryExercise(idHistoryExercise : String) : List<HistoryExerciseSetCacheEntity>

    @Query("SELECT count(*) FROM history_exercise_sets WHERE fk_id_history_exercise =:idHistoryExercise")
    suspend fun getTotalHistoryExerciseSet(idHistoryExercise: String): Int

}