package com.mikolove.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikolove.core.database.model.*
import com.mikolove.core.database.util.DateConverter

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
    GroupCacheEntity::class,
    ExerciseBodyPartCacheEntity::class,
    WorkoutGroupCacheEntity::class],
    version = 1,
    exportSchema = false)
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
    abstract fun workoutGroupDao() : GroupDao
    abstract fun groupWithWorkoutDao() : GroupWithWorkoutDao
    abstract fun exerciseBodyPartDao() : ExerciseBodyPartDao

    companion object{
        val DATABASE_NAME : String = "allmightworkout_db"
    }
}