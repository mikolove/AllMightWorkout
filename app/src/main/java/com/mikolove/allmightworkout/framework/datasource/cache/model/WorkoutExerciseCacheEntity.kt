package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "workouts_exercises",
    primaryKeys = ["id_workout", "id_exercise"],
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
data class WorkoutExerciseCacheEntity(

    @ColumnInfo(name = "id_workout")
    var idWorkout: String,

    @ColumnInfo(name = "id_exercise")
    var idExercise: String,

    @ColumnInfo(name = "created_at")
    var createdAt: Date
) {}
