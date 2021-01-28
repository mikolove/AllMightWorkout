package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*

@Entity(
    tableName = "body_parts",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = WorkoutTypeCacheEntity::class,
            parentColumns = arrayOf("id_workout_type"),
            childColumns = arrayOf("fk_id_workout_type"),
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

    @ColumnInfo(name = "fk_id_workout_type")
    var idWorkoutType : String,

    ){}

data class BodyPartWithWorkoutTypeCacheEntity(

    @Embedded
    val bodyPartCacheEntity : BodyPartCacheEntity,

    @Relation(
        parentColumn = "fk_id_workout_type",
        entityColumn = "id_workout_type"
    )
    val workoutTypeCacheEntity : WorkoutTypeCacheEntity
)