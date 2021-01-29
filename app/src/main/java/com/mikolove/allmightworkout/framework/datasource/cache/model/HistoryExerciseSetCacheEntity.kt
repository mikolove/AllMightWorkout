package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "history_exercise_sets",
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
    var idHistoryExerciseSet : String,

    @ColumnInfo(name = "fk_id_history_exercise")
    var idHistoryExercise : String?,

    @ColumnInfo(name = "reps")
    var reps : Int,

    @ColumnInfo(name = "weight")
    var weight : Int,

    @ColumnInfo(name = "duration")
    var time : Int,

    @ColumnInfo(name = "rest_time")
    var restTime : Int,

    @ColumnInfo(name = "started_at")
    var started_at : Date,

    @ColumnInfo(name ="ended_at")
    var ended_at : Date,

    @ColumnInfo(name = "created_at")
    var created_at : Date,

    @ColumnInfo(name = "updated_at")
    var updated_at : Date

    ) {
}