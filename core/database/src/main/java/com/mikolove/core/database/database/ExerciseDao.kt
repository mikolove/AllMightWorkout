package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mikolove.core.database.model.ExerciseCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao{

    @Upsert
    suspend fun upsertExercise(exercise: ExerciseCacheEntity) : Long

    @Upsert
    suspend fun upsertExercises(exercise: List<ExerciseCacheEntity>) : List<Long>

    @Query("DELETE FROM exercises WHERE id_exercise = :primaryKey")
    suspend fun removeExercise(primaryKey: String)  : Int

    @Query("DELETE FROM exercises WHERE id_exercise IN (:primaryKeys)")
    suspend fun removeExercises(primaryKeys: List<String>)  : List<Int>

    @Query("SELECT * FROM exercises WHERE id_exercise = :idUser")
    fun getExercises(idUser : String) : Flow<List<ExerciseCacheEntity>>

    @Query("SELECT * FROM exercises WHERE id_exercise = :primaryKey")
    suspend fun getExerciseById(primaryKey: String) : ExerciseCacheEntity
}