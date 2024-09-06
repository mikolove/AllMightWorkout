package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mikolove.core.database.model.HistoryExerciseSetCacheEntity

@Dao
interface HistoryExerciseSetDao{

    @Insert
    suspend fun insertHistoryExerciseSet(historyExerciseSet: HistoryExerciseSetCacheEntity) : Long
}