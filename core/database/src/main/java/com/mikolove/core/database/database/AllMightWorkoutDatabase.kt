package com.mikolove.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikolove.core.database.model.*
import com.mikolove.core.database.util.DateConverter
import com.mikolove.core.database.util.ExerciseTypeConverter
import com.mikolove.core.database.util.StringJsonConverter

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
@TypeConverters(
    DateConverter::class,
    ExerciseTypeConverter::class,
    StringJsonConverter::class)
abstract class AllMightWorkoutDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun workoutDao()  : WorkoutDao
    abstract fun exerciseDao() : ExerciseDao
    abstract fun exerciseSetDao() : ExerciseSetDao
    abstract fun bodyPartDao() : BodyPartDao
    abstract fun workoutTypeDao() : WorkoutTypeDao
    abstract fun analyticsDao() : AnalyticsDao
    abstract fun groupDao() : GroupDao
    abstract fun workoutGroupDao() : WorkoutGroupDao
    abstract fun exerciseBodyPartDao() : ExerciseBodyPartDao

}