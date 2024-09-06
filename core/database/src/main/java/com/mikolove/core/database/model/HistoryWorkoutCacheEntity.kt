package com.mikolove.core.database.model

import androidx.room.*
import java.time.ZonedDateTime
import java.util.*

@Entity(
    tableName = "history_workouts",
    indices = [Index("id_history_workout")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = UserCacheEntity::class,
            parentColumns = arrayOf("id_user"),
            childColumns = arrayOf("fk_id_user")
        )
    )
)
data class HistoryWorkoutCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_history_workout")
    var idHistoryWorkout: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "fk_id_user",index = true)
    var idUser: String,

    @ColumnInfo(name ="started_at")
    var startedAt: ZonedDateTime,

    @ColumnInfo(name ="ended_at")
    var endedAt: ZonedDateTime,

    @ColumnInfo(name ="created_at")
    var createdAt: ZonedDateTime,

) {}

data class HistoryWorkoutWithExercisesCacheEntity(

    @Embedded
    val historyWorkoutCacheEntity : HistoryWorkoutCacheEntity,

    @Relation(
        entity = HistoryExerciseCacheEntity::class,
        parentColumn = "id_history_workout",
        entityColumn = "fk_id_history_workout"
    )
    val listOfHistoryExercisesCacheEntity : List<HistoryExerciseWithSetsCacheEntity>
){}