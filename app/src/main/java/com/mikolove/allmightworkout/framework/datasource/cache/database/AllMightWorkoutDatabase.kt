package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikolove.allmightworkout.framework.datasource.cache.model.*
import com.mikolove.allmightworkout.framework.datasource.cache.util.DateConverter

@Database(entities = [
    UserCacheEntity::class,
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
    abstract fun userDao() : UserDao
    abstract fun workoutDao()  : WorkoutDao
    abstract fun exerciseDao() : ExerciseDao
    abstract fun exerciseSetDao() : ExerciseSetDao
    abstract fun bodyPartDao() : BodyPartDao
    abstract fun workoutTypeDao() : WorkoutTypeDao
    abstract fun historyWorkoutDao() : HistoryWorkoutDao
    abstract fun historyExerciseDao() : HistoryExerciseDao
    abstract fun historyExerciseSetDao() : HistoryExerciseSetDao
    abstract fun workoutExerciseDao() : WorkoutExerciseDao

    companion object{
        val DATABASE_NAME : String = "allmightworkout_db"
    }
}