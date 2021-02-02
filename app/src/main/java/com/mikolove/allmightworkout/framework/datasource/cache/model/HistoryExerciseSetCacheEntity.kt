package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "history_exercise_sets",
    indices = [Index("id_history_exercise_set")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = HistoryExerciseCacheEntity::class,
            parentColumns = arrayOf("id_history_exercise"),
            childColumns = arrayOf("fk_id_history_exercise"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class HistoryExerciseSetCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_history_exercise_set")
    var idHistoryExerciseSet: String,

    @ColumnInfo(name = "fk_id_history_exercise")
    var idHistoryExercise: String?,

    @ColumnInfo(name = "reps")
    var reps: Int,

    @ColumnInfo(name = "weight")
    var weight: Int,

    @ColumnInfo(name = "duration")
    var time: Int,

    @ColumnInfo(name = "rest_time")
    var restTime: Int,

    @ColumnInfo(name = "started_at")
    var startedAt: Date,

    @ColumnInfo(name ="ended_at")
    var endedAt: Date,

    @ColumnInfo(name = "created_at")
    var createdAt: Date,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Date

    ) {
}