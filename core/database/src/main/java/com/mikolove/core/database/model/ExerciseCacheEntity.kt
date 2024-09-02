package com.mikolove.core.database.model

import androidx.room.*
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseSet
import java.util.*

@Entity(
    tableName = "exercises",
    indices = [Index("id_exercise")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = BodyPartCacheEntity::class,
            parentColumns = arrayOf("id_body_part"),
            childColumns = arrayOf("fk_id_body_part")
        ),
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

    @ColumnInfo(name = "fk_id_body_part" ,index = true)
    var idBodyPart: String?,

    @ColumnInfo(name = "exercise_type")
    var exerciseType: String,

    @ColumnInfo(name = "is_active")
    var isActive: Boolean,

    @ColumnInfo(name = "fk_id_user",index = true)
    var idUser : String? = null,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: Date
){

    companion object{

        fun nullNameError() : String{
            return "You must enter a name"
        }

    }
}

data class ExerciseWithSetsCacheEntity(

    @Embedded val exercise : ExerciseCacheEntity,

    @Relation(
        parentColumn ="id_exercise",
        entityColumn = "id_exercise",
        associateBy = Junction(ExerciseSetCacheEntity::class)
    )
    val sets : List<ExerciseSet>
)