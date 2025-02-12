package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mikolove.core.database.model.DeletedExerciseSyncEntity
import com.mikolove.core.database.model.ExercisePendingSyncEntity

@Dao
interface ExercisePendingSyncDao {

    @Query("SELECT * FROM exercise_pending_syncs WHERE id_user = :idUser")
    suspend fun getAllExercisePendingSyncEntities(idUser : String) : List<ExercisePendingSyncEntity>

    @Query("SELECT * FROM exercise_pending_syncs WHERE id_pending_exercise = :idExercise")
    suspend fun getExercisePendingSyncEntity(idExercise : String) : ExercisePendingSyncEntity?

    @Upsert
    suspend fun upsertExercisePendingSyncEntity(entity: ExercisePendingSyncEntity)

    @Query("DELETE FROM exercise_pending_syncs WHERE id_pending_exercise = :idExercise")
    suspend fun deleteExercisePendingSyncEntity(idExercise : String)


    @Query("SELECT * FROM deleted_exercise_pending_syncs WHERE id_user = :idUser")
    suspend fun getAllDeletedExerciseSyncEntities(idUser: String) : List<DeletedExerciseSyncEntity>

    @Upsert
    suspend fun upsertDeletedExerciseSyncEntity(entity: DeletedExerciseSyncEntity)

    @Query("DELETE FROM deleted_exercise_pending_syncs WHERE id_deleted_exercise = :idExercise")
    suspend fun deleteDeletedExerciseSyncEntity(idExercise : String)
}