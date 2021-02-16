package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "workouts",
    indices = [Index("id_workout")])
data class WorkoutCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_workout")
    var idWorkout: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "is_active")
    var isActive: Boolean,

    @ColumnInfo(name = "exercise_ids_updated_at")
    var exerciseIdsUpdatedAt: Date?,

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

data class WorkoutWithExercisesCacheEntity(

    @Embedded
    val workoutCacheEntity: WorkoutCacheEntity,

    @Relation(
        entity = ExerciseCacheEntity::class,
        parentColumn = "id_workout",
        entityColumn = "id_exercise",
        associateBy = Junction(WorkoutExerciseCacheEntity::class)
    )
    val listOfExerciseCacheEntity : List<ExerciseWithSetsCacheEntity>?
)