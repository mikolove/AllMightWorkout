package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "workout_groups",
    indices = [Index("id_group")],
)
data class WorkoutGroupCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="id_group")
    var idGroup : String,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: Date
)