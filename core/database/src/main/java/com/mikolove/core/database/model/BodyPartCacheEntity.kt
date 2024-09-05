package com.mikolove.core.database.model

import androidx.room.*
import com.mikolove.core.domain.workout.Workout

@Entity(
    tableName = "body_parts",
    indices = [Index("id_body_part")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = WorkoutTypeCacheEntity::class,
            parentColumns = arrayOf("id_workout_type"),
            childColumns = arrayOf("fk_id_workout_type"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseBodyPartCacheEntity::class,
            parentColumns = arrayOf("id_body_part"),
            childColumns = arrayOf("id_body_part"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class BodyPartCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_body_part")
    var idBodyPart : String,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "fk_id_workout_type", index = true )
    var idWorkoutType : String?,

    )

data class BodyPartCacheEntityWithWorkoutType(
    @Embedded val bodyPart : BodyPartCacheEntity,

    @Relation(
        entity = WorkoutTypeCacheEntity::class,
        parentColumn = "fk_id_workout_type",
        entityColumn = "id_workout_type")
    val workoutTypeCacheEntity : WorkoutTypeCacheEntity
)