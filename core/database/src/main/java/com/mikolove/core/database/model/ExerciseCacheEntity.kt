package com.mikolove.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseType
import java.time.ZonedDateTime

@Entity(
    tableName = "exercises",
    indices = [Index("id_exercise")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = UserCacheEntity::class,
            parentColumns = arrayOf("id_user"),
            childColumns = arrayOf("fk_id_user")
        )
    )
)
data class ExerciseCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_exercise")
    var idExercise: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "exercise_type")
    var exerciseType: ExerciseType,

    @ColumnInfo(name = "is_active")
    var isActive: Boolean,

    @ColumnInfo(name = "fk_id_user",index = true)
    var idUser : String,

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

data class ExerciseWithBodyPartsCacheEntity(
    @Embedded val exercise : ExerciseCacheEntity,

    @Relation(
        entity = BodyPartCacheEntity::class,
        parentColumn = "id_exercise",
        entityColumn = "id_body_part",
        associateBy = Junction(ExerciseBodyPartCacheEntity::class)
    )
    val listOfBodyPartsCacheEntity : List<BodyPartCacheEntity>
)

data class ExerciseWithSetsCacheEntity(

    @Embedded val exercise : ExerciseCacheEntity,

    @Relation(
        entity = ExerciseSetCacheEntity::class,
        parentColumn ="id_exercise",
        entityColumn = "id_exercise",
        associateBy = Junction(ExerciseSetCacheEntity::class)
    )
    val listOfExerciseSetCacheEntity : List<ExerciseSetCacheEntity>,

    @Relation(
        entity = BodyPartCacheEntity::class,
        parentColumn = "id_exercise",
        entityColumn = "id_body_part",
        associateBy = Junction(ExerciseBodyPartCacheEntity::class)
    )
    val listOfBodyPartsCacheEntity : List<BodyPartCacheEntity>
)