package com.mikolove.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.ZonedDateTime
import java.util.Date

@Entity(
    tableName = "groups",
    indices = [Index("id_group")],
)
data class GroupCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="id_group")
    var idGroup : String,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: ZonedDateTime,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: ZonedDateTime
)

data class GroupsWithWorkoutsCacheEntity(

    @Embedded  val groupCacheEntity : GroupCacheEntity,

    @Relation(
        entity = WorkoutCacheEntity::class,
        parentColumn = "id_group",
        entityColumn = "id_workout",
        associateBy = Junction(WorkoutGroupCacheEntity::class)
    )
    val listOfWorkoutsCacheEntity : List<WorkoutWithExercisesCacheEntity>?,

    )
