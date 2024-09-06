package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.workout.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutCacheService {

    suspend fun upsertWorkout(workout: Workout, idUser : String) : Long

    suspend fun removeWorkouts(workouts : List<Workout>) : Int

    suspend fun getWorkoutById(primaryKey : String) : Workout

    suspend fun getWorkouts(idUser : String) : Flow<List<Workout>>

    suspend fun getWorkoutByWorkoutType(idWorkoutType : List<String>, idUser : String) : List<Workout>

    //link exercise
    suspend fun isExerciseInWorkout( idWorkout: String , idExercise: String ) : Boolean

    suspend fun addExerciseToWorkout(workoutId : String, exerciseId : String, set : ExerciseSet) : Long

    suspend fun removeExerciseFromWorkout(workoutId : String, exerciseId : String, sets : List<ExerciseSet>) : Int

    //link group
    suspend fun isWorkoutInGroup( idWorkout: String , idGroup: String ) : Boolean

    suspend fun addWorkoutToGroup(workoutId : String, groupId : String) : Long

    suspend fun removeWorkoutFromGroup(workoutId : String, groupId : String) : Int

}