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
}