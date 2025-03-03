package com.mikolove.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "exercises_bodypart",
    primaryKeys = ["id_exercise","id_body_part"],
    indices = [Index(value = ["id_exercise","id_body_part"], unique = true)],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ExerciseCacheEntity::class,
            parentColumns = arrayOf("id_exercise"),
            childColumns = arrayOf("id_exercise"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BodyPartCacheEntity::class,
            parentColumns = arrayOf("id_body_part"),
            childColumns = arrayOf("id_body_part"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class ExerciseBodyPartCacheEntity (

    @ColumnInfo(name="id_exercise",index = true)
    val idExercise : String,

    @ColumnInfo(name="id_body_part", index = true)
    val idBodyPart : String
)