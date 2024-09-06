package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mikolove.core.database.model.UserCacheEntity
import com.mikolove.core.database.model.UserWithWorkoutAndExerciseCacheEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user : UserCacheEntity) : Long

    @Query("SELECT * FROM users WHERE id_user = :primaryKey")
    suspend fun getUser(primaryKey: String) : UserCacheEntity?

    @Transaction
    @Query("SELECT * FROM users WHERE id_user = :primaryKey")
    suspend fun getUserWithWorkouts(primaryKey: String) : UserWithWorkoutAndExerciseCacheEntity?
}