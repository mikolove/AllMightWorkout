package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.workout.Workout
import kotlinx.coroutines.flow.Flow


interface WorkoutCacheDataSource {

    //workout
    suspend fun upsertWorkout(workout: Workout, idUser : String) : Long

    suspend fun upsertWorkouts(workouts: List<Workout>, idUser : String) : List<Long>

    suspend fun removeWorkout(workoutId : String) : Int

    suspend fun removeWorkouts(workoutIds : List<String>) : Int

    fun getWorkouts(idUser : String,searchQuery : String) : Flow<List<Workout>>

    suspend fun getWorkoutByWorkoutType(idWorkoutType : List<String>,idUser : String) : Flow<List<Workout>>

    suspend fun getWorkoutByWorkoutTypeByGroup(idWorkoutType : List<String>, idGroup : List<String>,idUser : String) : Flow<List<Workout>>

    suspend fun getWorkoutById(primaryKey : String) : Workout

    //link exercise
    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Boolean

    suspend fun addExerciseToWorkout(workoutId : String, exerciseId : String, exerciseSet : ExerciseSet) : Long

    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String, sets : List<ExerciseSet>) : Int

    //link group
    suspend fun isWorkoutInGroup( idWorkout: String , idGroup: String ) : Boolean

    suspend fun addWorkoutToGroup(workoutId : String, groupId : String) : Long

    suspend fun removeWorkoutFromGroup(workoutId : String, groupId : String) : Int
}