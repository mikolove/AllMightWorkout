package com.mikolove.core.database.model

import androidx.room.*
import java.time.ZonedDateTime

@Entity(
    tableName = "workouts",
    indices = [Index("id_workout")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = UserCacheEntity::class,
            parentColumns = arrayOf("id_user"),
            childColumns = arrayOf("fk_id_user")
        )
    )
)
data class WorkoutCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_workout")
    var idWorkout: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "is_active")
    var isActive: Boolean,

    @ColumnInfo(name = "fk_id_user", index = true)
    var idUser: String,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: ZonedDateTime,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: ZonedDateTime
){

    companion object{

        fun nullNameError() : String{
            return "You must enter a name"
        }

    }
}

data class WorkoutWithExercisesCacheEntity(

    @Embedded
    val workoutCacheEntity: WorkoutCacheEntity,

    @Relation(
        entity = ExerciseCacheEntity::class,
        parentColumn = "id_workout",
        entityColumn = "id_exercise",
        associateBy = Junction(ExerciseSetCacheEntity::class)
    )
    val listOfExerciseCacheEntity : List<ExerciseWithSetsCacheEntity>?,

    @Relation(
        entity = GroupCacheEntity::class,
        parentColumn = "id_workout",
        entityColumn = "id_group",
        associateBy = Junction(WorkoutGroupCacheEntity::class))
    val listOfGroupCacheEntity : List<GroupCacheEntity>?,

    @Relation(
        entity = UserCacheEntity::class,
        parentColumn = "fk_id_user",
        entityColumn = "id_user" )
    val userCacheEntity : UserCacheEntity
)

