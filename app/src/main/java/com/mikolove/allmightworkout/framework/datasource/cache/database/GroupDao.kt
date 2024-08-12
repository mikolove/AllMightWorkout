package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.mikolove.allmightworkout.framework.datasource.cache.model.GroupCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.GroupsWithWorkoutsCacheEntity

@Dao
interface GroupDao {

    @Upsert
    suspend fun upsertWorkoutGroup(workoutGroup : GroupCacheEntity) : Long

    @Insert
    suspend fun insertWorkoutGroup(workoutGroup : GroupCacheEntity) : Long

    @Query("SELECT * FROM groups WHERE id_group = :groupId ORDER BY name ASC")
    suspend fun getGroup(groupId : String) : GroupsWithWorkoutsCacheEntity

    @Delete
    suspend fun deleteWorkoutGroup(vararg workoutGroups: GroupCacheEntity) : Int

    @Query(" SELECT * FROM groups ORDER BY name ASC")
    suspend fun getWorkoutGroups() : List<GroupsWithWorkoutsCacheEntity>


}