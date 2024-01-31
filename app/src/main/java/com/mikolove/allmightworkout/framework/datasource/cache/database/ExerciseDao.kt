package com.mikolove.allmightworkout.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseCacheEntity
import com.mikolove.allmightworkout.framework.datasource.cache.model.ExerciseWithSetsCacheEntity
import java.util.*


const val EXERCISE_ORDER_ASC: String = ""
const val EXERCISE_ORDER_DESC: String = "-"
const val EXERCISE_FILTER_NAME = "name"
const val EXERCISE_FILTER_DATE_CREATED = "created_at"

const val EXERCISE_ORDER_BY_ASC_DATE_CREATED = EXERCISE_ORDER_ASC + EXERCISE_FILTER_DATE_CREATED
const val EXERCISE_ORDER_BY_DESC_DATE_CREATED = EXERCISE_ORDER_DESC + EXERCISE_FILTER_DATE_CREATED
const val EXERCISE_ORDER_BY_ASC_NAME = EXERCISE_ORDER_ASC + EXERCISE_FILTER_NAME
const val EXERCISE_ORDER_BY_DESC_NAME = EXERCISE_ORDER_DESC + EXERCISE_FILTER_NAME

const val EXERCISE_PAGINATION_PAGE_SIZE = 10

@Dao
interface ExerciseDao{

    @Insert
    suspend fun insertExercise(exercise: ExerciseCacheEntity) : Long

    @Query("""
        UPDATE exercises 
        SET 
        name = :name,
        fk_id_body_part = :idBodyPart,
        is_active = :isActive,
        exercise_type = :exerciseType,
        updated_at= :updatedAt
        WHERE id_exercise = :primaryKey
    """)
    suspend fun updateExercise(
        primaryKey: String,
        name: String,
        idBodyPart: String?,
        isActive: Boolean,
        exerciseType: String,
        updatedAt: Date
    ) : Int

    @Query("DELETE FROM exercises WHERE id_exercise = :primaryKey")
    suspend fun removeExerciseById(primaryKey :String) : Int

    @Transaction
    @Query("SELECT * FROM exercises WHERE id_exercise = :primaryKey")
    suspend fun getExerciseById(primaryKey: String) : ExerciseWithSetsCacheEntity?

    @Query("SELECT count(*) FROM exercises WHERE fk_id_user = :idUser")
    suspend fun getTotalExercises(idUser : String) : Int

    @Transaction
    @Query("""
        SELECT 
            exercises.id_exercise,
            exercises.name,
            exercises.fk_id_body_part,
            exercises.exercise_type,
            exercises.is_active,
            exercises.fk_id_user,
            exercises.created_at,
            exercises.updated_at
        FROM exercises, workouts, workouts_exercises 
        WHERE exercises.id_exercise = workouts_exercises.id_exercise
        AND workouts_exercises.id_workout = workouts.id_workout
        AND workouts.id_workout = :idWorkout
    """)
    suspend fun getExercisesByWorkout(idWorkout: String): List<ExerciseWithSetsCacheEntity>?

    @Transaction
    @Query("DELETE FROM exercises WHERE id_exercise IN (:primaryKeys)")
    suspend fun removeExercises(primaryKeys: List<String>)  : Int

    @Transaction
    @Query("SELECT * FROM exercises WHERE id_exercise = :idUser")
    suspend fun getExercises(idUser : String) : List<ExerciseWithSetsCacheEntity>

    @Transaction
    @Query("""
        SELECT * FROM exercises
        WHERE name LIKE '%' || :query || '%'
        AND fk_id_user = :idUser
        ORDER BY created_at DESC LIMIT ( :page * :pageSize)
    """)
    suspend fun getExercisesOrderByDateDESC(query: String, page: Int, idUser : String, pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE): List<ExerciseWithSetsCacheEntity>

    @Transaction
    @Query("""
        SELECT * FROM exercises
        WHERE name LIKE '%' || :query || '%'
        AND fk_id_user = :idUser
        ORDER BY created_at ASC LIMIT ( :page * :pageSize)
    """)
    suspend fun getExercisesOrderByDateASC(query: String, page: Int, idUser : String, pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE): List<ExerciseWithSetsCacheEntity>

    @Transaction
    @Query("""
        SELECT * FROM exercises
        WHERE name LIKE '%' || :query || '%'
        AND fk_id_user = :idUser
        ORDER BY name COLLATE NOCASE DESC LIMIT ( :page * :pageSize)
    """)
    suspend fun getExercisesOrderByNameDESC(query: String, page: Int, idUser : String, pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE): List<ExerciseWithSetsCacheEntity>

    @Transaction
    @Query("""
        SELECT * FROM exercises
        WHERE name LIKE '%' || :query || '%'
        AND fk_id_user = :idUser
        ORDER BY name COLLATE NOCASE ASC LIMIT ( :page * :pageSize)
    """)
    suspend fun getExercisesOrderByNameASC(query: String, page: Int, idUser : String, pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE): List<ExerciseWithSetsCacheEntity>

}

suspend fun ExerciseDao.returnOrderedQuery(
    query: String,
    filterAndOrder: String,
    page: Int,
    idUser: String
): List<ExerciseWithSetsCacheEntity>{

    when{

        filterAndOrder.contains(EXERCISE_ORDER_BY_DESC_DATE_CREATED) -> {
            return getExercisesOrderByDateDESC(
                query = query,
                page = page,
                idUser = idUser
            )
        }
        filterAndOrder.contains(EXERCISE_ORDER_BY_ASC_DATE_CREATED) -> {
            return getExercisesOrderByDateASC(
                query = query,
                page = page,
                idUser = idUser
            )
        }
        filterAndOrder.contains(EXERCISE_ORDER_BY_DESC_NAME) -> {
            return getExercisesOrderByNameDESC(
                query = query,
                page = page,
                idUser = idUser
            )
        }
        filterAndOrder.contains(EXERCISE_ORDER_BY_ASC_NAME) -> {
            return getExercisesOrderByNameASC(
                query = query,
                page = page,
                idUser = idUser
            )
        }
        else -> {
            return getExercisesOrderByDateDESC(
                query = query,
                page = page,
                idUser = idUser
            )
        }
    }
}