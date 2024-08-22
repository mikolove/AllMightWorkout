package com.mikolove.core.database.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "users",
    indices = [Index("id_user")])
data class UserCacheEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id_user")
    var idUser :String,

    @ColumnInfo(name = "name")
    var name :String?,

    @ColumnInfo(name = "email")
    var email : String?,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt : Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt : Date

    ){

}

data class UserWithWorkoutAndExerciseCacheEntity(

    @Embedded
    val userCacheEntity : UserCacheEntity,

    @Relation(
        entity = WorkoutCacheEntity::class,
        parentColumn = "id_user",
        entityColumn = "fk_id_user" )
    val listOfWorkoutCacheEntity : List<WorkoutWithExercisesCacheEntity>?
){

}
