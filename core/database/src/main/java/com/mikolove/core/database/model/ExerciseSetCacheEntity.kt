package com.mikolove.core.database.model

import androidx.room.*
import java.time.ZonedDateTime
import java.util.*

@Entity(
    tableName = "exercises_sets",
    primaryKeys = ["id_workout", "id_exercise","id_exercise_set"],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = WorkoutCacheEntity::class,
            parentColumns = arrayOf("id_workout"),
            childColumns = arrayOf("id_workout"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseCacheEntity::class,
            parentColumns = arrayOf("id_exercise"),
            childColumns = arrayOf("id_exercise"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class ExerciseSetCacheEntity(

    @ColumnInfo(name = "id_workout")
    var idWorkout: String,

    @ColumnInfo(name = "id_exercise",index = true)
    var idExercise: String,

    @ColumnInfo(name = "id_exercise_set")
    var idExerciseSet: String,

    @ColumnInfo(name = "reps")
    var reps: Int,

    @ColumnInfo(name = "weight")
    var weight: Int,

    @ColumnInfo(name = "duration")
    var time: Int,

    @ColumnInfo(name = "rest_time")
    var restTime: Int,

    @ColumnInfo(name = "position")
    var order: Int,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: ZonedDateTime,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: ZonedDateTime
) {}
