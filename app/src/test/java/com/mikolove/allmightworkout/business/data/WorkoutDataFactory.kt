package com.mikolove.allmightworkout.business.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mikolove.allmightworkout.business.domain.model.Workout

//No context in Test so create a Classloader to get the ressources
class WorkoutDataFactory(
    private val testClassLoader: ClassLoader
) {

    fun produceListOfWorkouts(): List<Workout>{
        val notes: List<Workout> = Gson()
            .fromJson(
                getWorkoutsFromFile("workout_list.json"),
                object: TypeToken<List<Workout>>() {}.type
            )
        return notes
    }

    fun produceHashMapOfWorkouts(workoutList: List<Workout>): HashMap<String, Workout>{
        val map = HashMap<String, Workout>()
        for(workout in workoutList){
            map.put(workout.idWorkout, workout)
        }
        return map
    }

    fun produceEmptyListOfWorkouts(): List<Workout>{
        return ArrayList()
    }

    fun getWorkoutsFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }

}