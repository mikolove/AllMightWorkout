package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
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

    @ColumnInfo(name = "fk_id_body_part")
    var idBodyPart: String?,

    @ColumnInfo(name = "exercise_type")
    var exerciseType: String,

    @ColumnInfo(name = "is_active")
    var isActive: Boolean,

    @ColumnInfo(name = "fk_id_user")
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
    @Embedded
    val exerciseCacheEntity : ExerciseCacheEntity,

    @Relation(
        entity = BodyPartCacheEntity::class,
        parentColumn = "fk_id_body_part",
        entityColumn = "id_body_part"
    )
    val bodyPartCacheEntity : BodyPartCacheEntity?,

    @Relation(
        entity = ExerciseSetCacheEntity::class,
        parentColumn = "id_exercise",
        entityColumn = "fk_id_exercise")
    val listOfExerciseSetCacheEntity : List<ExerciseSetCacheEntity>?
)

/*
data class ExerciseWithWorkoutsCacheEntity(

    @Embedded
    val exerciseCacheEntity: ExerciseCacheEntity,

    @Relation(
        parentColumn = "fk_id_body_part",
        entityColumn = "id_body_part"
    )
    val bodyPartCacheEntity : BodyPartCacheEntity,

    @Relation(
        parentColumn = "id_exercise",
        entityColumn = "fk_id_exercise")
    val listOfExerciseSetCacheEntity : List<ExerciseSetCacheEntity>,

    @Relation(
        parentColumn = "id_exercise",
        entityColumn = "id_exercise",
        associateBy = Junction(WorkoutExerciseCacheEntity::class)
    )
    val listOfWorkoutsCacheEntity : List<WorkoutCacheEntity>
)
*/
