package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
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

data class HistoryWorkoutWithExercisesCacheEntity(

    @Embedded
    val historyWorkoutCacheEntity : HistoryWorkoutCacheEntity,

    @Relation(
        parentColumn = "id_history_workout",
        entityColumn = "fk_id_history_workout"
    )
    val listOfHistoryExercisesCacheEntity : List<HistoryExerciseWithSetsCacheEntity>
){}