package com.mikolove.core.database.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "history_exercises",
    indices = [Index("id_history_exercise")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = HistoryWorkoutCacheEntity::class,
            parentColumns = arrayOf("id_history_workout"),
            childColumns = arrayOf("fk_id_history_workout"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class HistoryExerciseCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_history_exercise")
    var idHistoryExercise: String,

    @ColumnInfo(name = "fk_id_history_workout",index = true)
    var idHistoryWorkout: String?,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "body_part")
    var bodyPart: String,

    @ColumnInfo(name = "workout_type")
    var workoutType: String,

    @ColumnInfo(name = "exercise_type")
    var exerciseType: String,

    @ColumnInfo(name ="started_at")
    var startedAt: Date,

    @ColumnInfo(name = "ended_at")
    var endedAt: Date,

    @ColumnInfo(name = "created_at")
    var createdAt: Date,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Date

) {}

data class HistoryExerciseWithSetsCacheEntity(

    @Embedded
    val historyExerciseCacheEntity : HistoryExerciseCacheEntity,

    @Relation(
        entity = HistoryExerciseSetCacheEntity::class,
        parentColumn = "id_history_exercise",
        entityColumn = "fk_id_history_exercise")
    val listOfHistoryExerciseSetsCacheEntity : List<HistoryExerciseSetCacheEntity>?
)

