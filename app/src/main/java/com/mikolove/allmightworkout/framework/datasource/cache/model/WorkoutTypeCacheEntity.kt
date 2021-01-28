package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_types"
)
data class WorkoutTypeCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_workout_type")
    var idWorkoutType : String,

    @ColumnInfo(name = "name")
    var name : String,

    ) {
}