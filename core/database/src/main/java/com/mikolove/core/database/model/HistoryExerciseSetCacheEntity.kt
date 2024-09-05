package com.mikolove.core.database.model

import androidx.room.*
import java.time.ZonedDateTime


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

    @ColumnInfo(name = "fk_id_history_exercise",index = true)
    var idHistoryExercise: String,

    @ColumnInfo(name = "reps")
    var reps: Int,

    @ColumnInfo(name = "weight")
    var weight: Int,

    @ColumnInfo(name = "duration")
    var time: Int,

    @ColumnInfo(name = "rest_time")
    var restTime: Int,

    @ColumnInfo(name = "started_at")
    var startedAt: ZonedDateTime,

    @ColumnInfo(name ="ended_at")
    var endedAt: ZonedDateTime,

    @ColumnInfo(name = "created_at")
    var createdAt: ZonedDateTime,

    @ColumnInfo(name = "updated_at")
    var updatedAt: ZonedDateTime

    ) {
}