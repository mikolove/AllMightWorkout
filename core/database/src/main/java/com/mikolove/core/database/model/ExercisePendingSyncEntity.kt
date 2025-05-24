package com.mikolove.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_pending_syncs",
    indices = [
        Index(value = ["id_user"]),
        Index(value = ["id_exercise"]),
    ]
)
data class ExercisePendingSyncEntity(
    @Embedded val exercise : ExerciseCacheEntity,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="id_pending_exercise")
    val idExercise : String = exercise.idExercise,

    @ColumnInfo(name ="id_user")
    val idUser : String
)