package com.mikolove.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.ZonedDateTime
import java.util.Date

@Entity(
    tableName = "groups",
    indices = [Index("id_group")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = UserCacheEntity::class,
            parentColumns = arrayOf("id_user"),
            childColumns = arrayOf("fk_id_user")
        )
    )
)
data class GroupCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="id_group")
    var idGroup : String,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "fk_id_user",index = true)
    var idUser : String,
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
