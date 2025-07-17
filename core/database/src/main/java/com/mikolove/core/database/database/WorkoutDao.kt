package com.mikolove.core.database.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mikolove.core.database.model.WorkoutCacheEntity
import com.mikolove.core.database.model.WorkoutWithExercisesCacheEntity
import com.mikolove.core.database.model.WorkoutWithExercisesWithGroupsCacheEntity
import com.mikolove.core.domain.workout.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Upsert
    suspend fun upsertWorkout(workout : WorkoutCacheEntity) : Long

    @Upsert
    suspend fun upsertWorkouts(workout : List<WorkoutCacheEntity>) : List<Long>

    @Query("DELETE FROM workouts WHERE id_workout = :id")
    suspend fun removeWorkout(id : String) : Int

    @Query("DELETE FROM workouts WHERE id_workout IN (:ids)")
    suspend fun removeWorkouts(ids : List<String>) : Int

    @Transaction
    @Query("SELECT * FROM workouts WHERE id_workout = :primaryKey")
    suspend fun getWorkoutById(primaryKey: String) : WorkoutWithExercisesWithGroupsCacheEntity

    @Transaction
    @Query("SELECT * FROM workouts WHERE id_workout = :primaryKey")
    fun getWorkout(primaryKey: String) : Flow<WorkoutWithExercisesWithGroupsCacheEntity>

    @Transaction
    @Query("SELECT * FROM workouts WHERE fk_id_user = :idUser AND name LIKE '%' || :name || '%'")
    fun getWorkouts(idUser: String, name: String) : Flow<List<WorkoutWithExercisesCacheEntity>>

    @Transaction
    @Query("""SELECT w.id_workout, w.name, w.is_active, w.fk_id_user, w.created_at, w.updated_at,e.id_exercise,e.name,e.exercise_type,e.is_active,e.fk_id_user,e.created_at,e.updated_at, bp.id_body_part,bp.name,bp.fk_id_workout_type   
        FROM workouts w, exercises e, exercises_bodypart eb, body_parts bp, workout_types wt
        WHERE e.id_exercise = eb.id_exercise
         AND eb.id_body_part = bp.id_body_part
         AND bp.fk_id_workout_type IN (:ids)
         AND w.fk_id_user=:idUser
         GROUP BY w.id_workout""")
    fun getWorkoutsWithExercises(ids : List<String>, idUser: String) : Flow<List<WorkoutWithExercisesCacheEntity>>

    @Transaction
    @Query("SELECT * " +
            "FROM workouts w " +
            "LEFT JOIN  exercises_sets es " +
            "ON w.id_workout = es.id_workout " +
            "LEFT JOIN exercises e " +
            "ON es.id_exercise = e.id_exercise " +
            "LEFT JOIN exercises_bodypart eb " +
            "ON e.id_exercise = eb.id_exercise " +
            "LEFT JOIN body_parts bp " +
            "ON eb.id_body_part = bp.id_body_part " +
            "LEFT JOIN workout_group wg " +
            "ON wg.id_workout = w.id_workout " +
            "LEFT JOIN `groups` g " +
            "ON wg.id_group = g.id_group " +
            "WHERE w.fk_id_user= :idUser" )
    fun getWorkoutsWithExercises( idUser: String) : Flow<List<WorkoutWithExercisesWithGroupsCacheEntity>>

    @Transaction
    @Query("SELECT * " +
            "FROM workouts w " +
            "LEFT JOIN  exercises_sets es " +
            "ON w.id_workout = es.id_workout " +
            "LEFT JOIN exercises e " +
            "ON es.id_exercise = e.id_exercise " +
            "LEFT JOIN exercises_bodypart eb " +
            "ON e.id_exercise = eb.id_exercise " +
            "LEFT JOIN body_parts bp " +
            "ON eb.id_body_part = bp.id_body_part " +
            "LEFT JOIN workout_group wg " +
            "ON wg.id_workout = w.id_workout " +
            "LEFT JOIN `groups` g " +
            "ON wg.id_group = g.id_group " +
            "WHERE g.id_group IN (:idGroups) " +
            "AND w.fk_id_user= :idUser" )
    fun getWorkoutsWithExercisesWithGroups( idGroups : List<String>, idUser: String) : Flow<List<WorkoutWithExercisesWithGroupsCacheEntity>>


    @Transaction
    @Query("SELECT * " +
            "FROM workouts w " +
            "LEFT JOIN  exercises_sets es " +
            "ON w.id_workout = es.id_workout " +
            "LEFT JOIN exercises e " +
            "ON es.id_exercise = e.id_exercise " +
            "LEFT JOIN exercises_bodypart eb " +
            "ON e.id_exercise = eb.id_exercise " +
            "LEFT JOIN body_parts bp " +
            "ON eb.id_body_part = bp.id_body_part " +
            "LEFT JOIN workout_group wg " +
            "ON wg.id_workout = w.id_workout " +
            "LEFT JOIN `groups` g " +
            "ON wg.id_group = g.id_group " +
            "WHERE g.id_group IN (:idGroups) " +
            "AND bp.fk_id_workout_type IN (:idWorkoutTypes) " +
            "AND w.fk_id_user= :idUser" )
    fun getWorkoutsWithExercisesWithGroupsWithWorkoutTypes(idWorkoutTypes : List<String>, idGroups : List<String>, idUser: String) : Flow<List<WorkoutWithExercisesWithGroupsCacheEntity>>

}