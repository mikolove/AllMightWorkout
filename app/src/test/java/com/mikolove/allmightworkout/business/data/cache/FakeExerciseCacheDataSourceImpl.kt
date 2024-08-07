package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.database.EXERCISE_PAGINATION_PAGE_SIZE


const val FORCE_NEW_EXERCISE_EXCEPTION = "FORCE_NEW_EXERCISE_EXCEPTION"
const val FORCE_UPDATE_EXERCISE_EXCEPTION = "FORCE_UPDATE_EXERCISE_EXCEPTION"
const val FORCE_DELETE_EXERCISE_EXCEPTION = "FORCE_DELETE_EXERCISE_EXCEPTION"
const val FORCE_DELETES_EXERCISE_EXCEPTION = "FORCE_DELETES_EXERCISE_EXCEPTION"
const val FORCE_SEARCH_EXERCISES_EXCEPTION = "FORCE_SEARCH_EXERCISES_EXCEPTION"
const val FORCE_NEW_ADD_EXERCISE_WORKOUT_EXCEPTION = "FORCE_NEW_ADD_EXERCISE_WORKOUT_EXCEPTION"
const val FORCE_REMOVE_EXERCISE_WORKOUT_EXCEPTION = "FORCE_REMOVE_EXERCISE_WORKOUT_EXCEPTION"
const val FORCE_GET_EXERCISE_BY_ID_EXCEPTION = "FORCE_GET_EXERCISE_BY_ID_EXCEPTION"

class FakeExerciseCacheDataSourceImpl(
    private val exercisesData: HashMap<String, Exercise>,
    private val workoutsData : HashMap<String, Workout>,
    private val dateUtil: DateUtil
): ExerciseCacheDataSource {


    override suspend fun insertExercise(exercise: Exercise): Long {
        if(exercise.idExercise.equals(FORCE_NEW_EXERCISE_EXCEPTION)){
            throw Exception("Something went wrong inserting the exercise.")
        }
        if(exercise.idExercise.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        exercisesData.put(exercise.idExercise, exercise)
        return 1 // success
    }

    override suspend fun updateExercise(
        primaryKey: String,
        name: String,
        bodyPart: BodyPart?,
        isActive: Boolean,
        exerciseType: String,
        updatedAt: String
    ): Int {

        if(primaryKey.equals(FORCE_UPDATE_EXERCISE_EXCEPTION)){
            throw java.lang.Exception("Something went wrong updating exercise.")
        }

        val updatedExercise = Exercise(
            idExercise = primaryKey,
            name = name,
            bodyPart = bodyPart,
            sets = listOf(),
            isActive = isActive,
            exerciseType = ExerciseType.valueOf(exerciseType),
            startedAt = null,
            endedAt = null,
            createdAt = exercisesData[primaryKey]?.createdAt ?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp()
        )

        return exercisesData[primaryKey]?.let {
            exercisesData.put(primaryKey,updatedExercise)
            1 //sucess
        }?: -1 //fail
    }

    override suspend fun removeExerciseById(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_EXERCISE_EXCEPTION)){
            throw Exception("Something went wrong deleting the exercise")
        }
        if(primaryKey.equals(FORCE_DELETES_EXERCISE_EXCEPTION)){
            throw Exception("Something went wrong deleting exercises")
        }

        return exercisesData.remove(primaryKey)?.let{
           1
        }?: -1
    }

    override suspend fun getExercises(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Exercise> {
        if(query.equals(FORCE_SEARCH_EXERCISES_EXCEPTION)){
            throw Exception("Something went wrong searching for exercises")
        }
        val results : ArrayList<Exercise> = ArrayList()
        for(exercise in exercisesData.values){
            if(exercise.name.contains(query)){
                results.add(exercise)
            }
            if(results.size > ( page * EXERCISE_PAGINATION_PAGE_SIZE)){
                break
            }
        }
        return results
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise? {
        if(primaryKey.equals(FORCE_GET_EXERCISE_BY_ID_EXCEPTION)){
            throw Exception("Something went wrong retrieving exercise by id.")
        }
        return exercisesData[primaryKey]
    }

    override suspend fun getTotalExercises(): Int {
        return exercisesData.size
    }

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? {
        return workoutsData[idWorkout]?.exercises
    }

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String): Long {
        if(idExercise.equals(FORCE_NEW_ADD_EXERCISE_WORKOUT_EXCEPTION)){
            throw Exception("Something went wrong adding exercise to workout.")
        }
        if(idExercise.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }

        return workoutsData[idWorkout]?.let {  workout ->

            val newListOfExercise : ArrayList<Exercise> = ArrayList()
            workout.exercises?.let { listExercise ->
                newListOfExercise.addAll(listExercise)
            }
            exercisesData[idExercise]?.let {
                newListOfExercise.add(it)
            }
            workout.exercises = newListOfExercise

            1
        }?: -1
    }

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String): Int {
        if(idExercise.equals(FORCE_REMOVE_EXERCISE_WORKOUT_EXCEPTION)){
            throw Exception("Something went wrong removing exercise from workout.")
        }
        if(idExercise.equals(FORCE_GENERAL_FAILURE)){
            return -1 // fail
        }
        return workoutsData[idWorkout]?.let {  workout ->

            workout.exercises?.let { listExercise ->

                val newListOfExercise : ArrayList<Exercise> = ArrayList()

                newListOfExercise.addAll(listExercise)
                newListOfExercise.remove(exercisesData[idExercise])

                workout.exercises = newListOfExercise
            }
            1
        }?: -1

    }

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int {
        val exercise = workoutsData[idWorkout]?.exercises?.find {
            it.idExercise == idExercise
        }

        return if(exercise !=null) 1 else 0
    }

    override suspend fun removeExercises(exercises: List<Exercise>): Int {
        exercises.let { listExercise ->
            for(exercise in listExercise){
                exercisesData.remove(exercise.idExercise)
            }
            return exercises.size
        }
        return -1
    }

}