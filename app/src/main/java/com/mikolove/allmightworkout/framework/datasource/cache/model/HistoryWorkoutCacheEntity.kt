package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "history_workouts"
)
data class HistoryWorkoutCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_history_workout")
    var idHistoryWorkout : String,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name ="started_at")
    var started_at : Date,

    @ColumnInfo(name ="ended_at")
    var ended_at : Date,

    @ColumnInfo(name ="created_at")
    var created_at : Date,

    @ColumnInfo(name ="updated_at")
    var updated_at : Date,

) {}