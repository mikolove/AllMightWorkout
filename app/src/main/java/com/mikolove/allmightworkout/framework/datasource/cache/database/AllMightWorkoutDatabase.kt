package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.framework.datasource.cache.model.*
import com.mikolove.allmightworkout.framework.datasource.cache.util.DateConverter

@Database(entities = [
    WorkoutTypeCacheEntity::class,
    BodyPartCacheEntity::class,
    WorkoutCacheEntity::class,
    ExerciseCacheEntity::class,
    ExerciseSetCacheEntity::class,
    HistoryWorkoutCacheEntity::class,
    HistoryExerciseCacheEntity::class,
    HistoryExerciseSetCacheEntity::class,
    WorkoutExerciseCacheEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AllMightWorkoutDatabase : RoomDatabase(){

    abstract fun workoutDao() : WorkoutDao

    companion object{
        val DATABASE_NAME : String = "allmightworkout_db"
    }
}