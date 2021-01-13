package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.database.EXERCISE_PAGINATION_PAGE_SIZE


const val FORCE_NEW_EXERCISE_EXCEPTION = "FORCE_NEW_EXERCISE_EXCEPTION"
const val FORCE_UPDATE_EXERCISE_EXCEPTION = "FORCE_UPDATE_EXERCISE_EXCEPTION"
const val FORCE_DELETE_EXERCISE_EXCEPTION = "FORCE_DELETE_EXERCISE_EXCEPTION"
const val FORCE_DELETES_EXERCISE_EXCEPTION = "FORCE_DELETES_EXERCISE_EXCEPTION"
const val FORCE_SEARCH_EXERCISES_EXCEPTION = "FORCE_SEARCH_EXERCISES_EXCEPTION"

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
        bodyPart: BodyPart,
        isActive: Boolean,
        exerciseType: String
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
            created_at = exercisesData[primaryKey]?.created_at?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
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
        return exercisesData[primaryKey]
    }

    override suspend fun getTotalExercises(): Int {
        return exercisesData.size
    }

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? {
        return workoutsData[idWorkout]?.exercises
    }

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String): Long {
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
        return if (workoutsData[idWorkout]?.exercises?.contains(exercisesData[idExercise]) == true) 1 else -1
    }
}