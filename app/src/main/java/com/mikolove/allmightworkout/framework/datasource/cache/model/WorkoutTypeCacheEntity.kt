package com.mikolove.allmightworkout.framework.datasource.cache.model

import androidx.room.*

@Entity(
    tableName = "workout_types",
    indices = [Index("id_workout_type")]
)
data class WorkoutTypeCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_workout_type")
    var idWorkoutType : String,

    @ColumnInfo(name = "name")
    var name : String,

    ) {
}

data class WorkoutTypeWithBodyPartCacheEntity(
    @Embedded
    val workoutTypeCacheEntity: WorkoutTypeCacheEntity,

    @Relation(
        parentColumn = "id_workout_type",
        entityColumn = "fk_id_workout_type"
    )
    val listOfBodyPartCacheEntity: List<BodyPartCacheEntity>?
)