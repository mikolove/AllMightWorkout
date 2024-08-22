package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mikolove.allmightworkout.framework.datasource.cache.model.UserCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.UserWithWorkoutAndExerciseCacheEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user : UserCacheEntity) : Long

    @Query("UPDATE users SET name = :name, updated_at = CURRENT_TIMESTAMP WHERE id_user = :primaryKey")
    suspend fun updateName(name : String, primaryKey: String) : Int

    @Query("SELECT * FROM users WHERE id_user = :primaryKey")
    suspend fun getUser(primaryKey: String) : UserCacheEntity?

    @Transaction
    @Query("SELECT * FROM users WHERE id_user = :primaryKey")
    suspend fun getUserWithWorkout(primaryKey: String) : UserWithWorkoutAndExerciseCacheEntity?
}