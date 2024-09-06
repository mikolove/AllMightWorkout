package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mikolove.core.database.model.GroupCacheEntity
import com.mikolove.core.database.model.GroupsWithWorkoutsCacheEntity

@Dao
interface GroupDao {

    @Upsert
    suspend fun upsertGroup(workoutGroup : GroupCacheEntity) : Long

    @Delete
    //suspend fun deleteGroups(vararg workoutGroups: GroupCacheEntity) : Int
    suspend fun deleteGroups(ids : List<String>) : Int

    @Transaction
    @Query(" SELECT * FROM groups WHERE fk_id_user = :idUser ORDER BY name ASC")
    suspend fun getGroups(idUser : String) : List<GroupsWithWorkoutsCacheEntity>

    @Transaction
    @Query("SELECT * FROM groups WHERE id_group = :groupId ORDER BY name ASC")
    suspend fun getGroup(groupId : String) : GroupsWithWorkoutsCacheEntity

}