package com.mikolove.allmightworkout.business.data.network

import com.mikolove.core.data.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.workout.Workout

class FakeExerciseNetworkDataSourceImpl(
    private val workoutsData : HashMap<String, Workout>,
    private val exercisesData : HashMap<String, Exercise>,
    private val deletedExercises: HashMap<String, Exercise>
) : ExerciseNetworkDataSource {

    override suspend fun insertExercise(exercise: Exercise) {
        exercisesData.put(exercise.idExercise,exercise)
    }

    override suspend fun updateExercise(exercise: Exercise) {
        exercisesData[exercise.idExercise] = exercise
    }

    override suspend fun removeExerciseById(primaryKey: String) {
        exercisesData.remove(primaryKey)
    }

    override suspend fun getExercises(): List<Exercise> {
        return ArrayList<Exercise>(exercisesData.values)
    }

    override suspend fun getTotalExercises(): Int {
        return exercisesData.size
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise? {
        return exercisesData[primaryKey]
    }

    override suspend fun addExerciseToWorkout(idWorkout: String, idExercise: String) {
        workoutsData[idWorkout]?.let { workout ->

            val listExercise : ArrayList<Exercise> = ArrayList()
            workout.exercises?.let { list ->
                for(exercise in list){
                    listExercise.add(exercise)
                }
            }

            exercisesData[idExercise]?.let {
                listExercise.add(it)
            }

            workout.exercises = listExercise
        }
    }

    override suspend fun removeExerciseFromWorkout(idWorkout: String, idExercise: String) {
        workoutsData[idWorkout]?.let { workout ->

            val listExercise : ArrayList<Exercise> = ArrayList()
            workout.exercises?.let { list ->
                for(exercise in list){
                    if(exercise.idExercise != idExercise)
                        listExercise.add(exercise)
                }
            }
            workout.exercises = listExercise
        }
    }

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Int {
        val exercise = workoutsData[idWorkout]?.exercises?.find {
            it.idExercise == idExercise
        }

        return if(exercise !=null) 1 else 0
    }

    override suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>? {
        return workoutsData[idWorkout]?.exercises
    }


    override suspend fun getDeletedExercises(): List<Exercise> {
        return ArrayList(deletedExercises.values)
    }

    override suspend fun insertDeletedExercise(exercise: Exercise) {
        deletedExercises.put(exercise.idExercise,exercise)
    }

    override suspend fun insertDeletedExercises(exercises: List<Exercise>) {
        exercises.forEach { exercise ->
            deletedExercises.put(exercise.idExercise,exercise)
        }
    }
}