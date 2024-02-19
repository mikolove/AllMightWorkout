package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mikolove.allmightworkout.business.domain.model.WorkoutGroup
import com.mikolove.allmightworkout.framework.datasource.cache.model.WorkoutGroupCacheEntity

@Dao
interface WorkoutGroupDao {

    @Insert
    suspend fun insertWorkoutGroup(workoutGroup : WorkoutGroupCacheEntity) : Long

    @Update
    suspend fun updateWorkoutGroup(workoutGroup: WorkoutGroupCacheEntity) : Int

    @Delete
    suspend fun deleteWorkoutGroup(vararg workoutGroups: WorkoutGroupCacheEntity) : Int

    @Query(" SELECT * FROM workout_groups ORDER BY name ASC")
    suspend fun getWorkoutGroups() : List<WorkoutGroupCacheEntity>
}