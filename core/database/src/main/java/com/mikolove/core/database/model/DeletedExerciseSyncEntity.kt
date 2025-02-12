package com.mikolove.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "deleted_exercise_pending_syncs",
)
data class DeletedExerciseSyncEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="id_deleted_exercise")
    val idExercise : String,
    @ColumnInfo(name ="id_user")
    val idUser : String
)