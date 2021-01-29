package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*
import java.util.*

@Entity(tableName = "workouts")
data class WorkoutCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_workout")
    var idWorkout : String,

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "is_active")
    var isActive : Boolean,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var created_at : Date,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updated_at : Date
){}

data class WorkoutWithExercisesCacheEntity(

    @Embedded
    val workoutCacheEntity: WorkoutCacheEntity,

    @Relation(
        parentColumn = "id_workout",
        entityColumn = "id_workout",
        associateBy = Junction(WorkoutExerciseCacheEntity::class)
    )
    val listOfExerciseCacheEntity : List<ExerciseWithSetsCacheEntity>
)