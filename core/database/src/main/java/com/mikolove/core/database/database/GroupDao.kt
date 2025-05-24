package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mikolove.core.database.model.GroupCacheEntity
import com.mikolove.core.database.model.GroupsWithWorkoutsCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Upsert
    suspend fun upsertGroup(workoutGroup : GroupCacheEntity) : Long

    @Upsert
    suspend fun upsertGroups(workoutGroups : List<GroupCacheEntity>) : List<Long>

    @Query("DELETE FROM `groups` WHERE id_group = :id AND fk_id_user = :idUser")
    suspend fun deleteGroup(id : String,idUser: String) : Int

    @Query("DELETE FROM `groups` WHERE id_group IN (:ids) AND fk_id_user = :idUser")
    suspend fun deleteGroups(ids : List<String>,idUser: String) : Int

    @Transaction
    @Query(" SELECT * FROM `groups` WHERE fk_id_user = :idUser ORDER BY name ASC")
    fun getGroups(idUser : String) : Flow<List<GroupsWithWorkoutsCacheEntity>>

    @Transaction
    @Query("SELECT * FROM `groups` WHERE id_group = :groupId ORDER BY name ASC")
    suspend fun getGroup(groupId : String) : GroupsWithWorkoutsCacheEntity

}