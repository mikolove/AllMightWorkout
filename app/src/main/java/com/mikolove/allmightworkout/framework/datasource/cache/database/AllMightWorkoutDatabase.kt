package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutCacheEntity

@Database(entities = [WorkoutCacheEntity::class], version = 1)
abstract class AllMightWorkoutDatabase : RoomDatabase(){

    abstract fun workoutDao() : WorkoutDao

    companion object{
        val DATABASE_NAME : String = "allmightworkout_db"
    }
}