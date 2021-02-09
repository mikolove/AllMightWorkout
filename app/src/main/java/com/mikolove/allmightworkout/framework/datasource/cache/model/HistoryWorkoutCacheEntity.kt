package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "history_workouts",
    indices = [Index("id_history_workout")]
)
data class HistoryWorkoutCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_history_workout")
    var idHistoryWorkout: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name ="started_at")
    var startedAt: Date,

    @ColumnInfo(name ="ended_at")
    var endedAt: Date,

    @ColumnInfo(name ="created_at")
    var createdAt: Date,

    @ColumnInfo(name ="updated_at")
    var updatedAt: Date,

) {}

data class HistoryWorkoutWithExercisesCacheEntity(

    @Embedded
    val historyWorkoutCacheEntity : HistoryWorkoutCacheEntity,

    @Relation(
        entity = HistoryExerciseCacheEntity::class,
        parentColumn = "id_history_workout",
        entityColumn = "fk_id_history_workout"
    )
    val listOfHistoryExercisesCacheEntity : List<HistoryExerciseWithSetsCacheEntity>?
){}