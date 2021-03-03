package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "exercise_sets",
    indices = [Index("id_exercise_set")],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ExerciseCacheEntity::class,
            parentColumns = arrayOf("id_exercise"),
            childColumns = arrayOf("fk_id_exercise"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class ExerciseSetCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_exercise_set")
    var idExerciseSet: String,

    @ColumnInfo(name = "fk_id_exercise")
    var idExercise: String?,

    @ColumnInfo(name = "reps")
    var reps: Int,

    @ColumnInfo(name = "weight")
    var weight: Int,

    @ColumnInfo(name = "duration")
    var time: Int,

    @ColumnInfo(name = "rest_time")
    var restTime: Int,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var createdAt: Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updatedAt: Date
){

    companion object{

        fun notEmptyError() : String{
            return "You must at least have one set"
        }

    }
}