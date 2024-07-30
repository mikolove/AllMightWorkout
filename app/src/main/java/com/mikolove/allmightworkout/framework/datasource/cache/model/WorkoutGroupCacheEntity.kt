package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "workout_by_group",
    primaryKeys = [ "id_workout" , "id_group" ],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = WorkoutCacheEntity::class,
            parentColumns = arrayOf("id_workout"),
            childColumns = arrayOf("id_workout"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GroupCacheEntity::class,
            parentColumns = arrayOf("id_group"),
            childColumns = arrayOf("id_group"),
            onDelete = ForeignKey.CASCADE
        )
    ))
data class WorkoutGroupCacheEntity(

    @ColumnInfo(name = "id_workout")
    var idWorkout : String,

    @ColumnInfo(name = "id_group")
    var idGroup : String,

    @ColumnInfo(name ="created_at")
    var createdAt : Date
)