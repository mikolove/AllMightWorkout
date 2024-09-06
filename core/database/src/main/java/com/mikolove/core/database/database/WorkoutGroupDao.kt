package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikolove.core.database.model.WorkoutGroupCacheEntity

@Dao
interface WorkoutGroupDao{

    @Query("SELECT COUNT(*) FROM workout_group WHERE id_workout = :idWorkout AND id_group = :idGroup" )
    suspend fun isWorkoutInGroup( idWorkout: String , idGroup: String ) : Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWorkoutToGroup(workoutGroupCacheEntity : WorkoutGroupCacheEntity) : Long

    @Query("DELETE FROM workout_group WHERE id_workout =:workoutId AND id_group = :groupId")
    suspend fun removeWorkoutFromGroup(workoutId : String, groupId : String) : Int

}