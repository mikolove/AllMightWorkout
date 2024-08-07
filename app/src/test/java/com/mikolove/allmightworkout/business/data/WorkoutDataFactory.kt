package com.mikolove.allmightworkout.business.data

import com.mikolove.core.domain.workout.Workout

//No context in Test so create a Classloader to get the ressources
class WorkoutDataFactory(
    testClassLoader: ClassLoader,
    filename: String
) : JsonDataFactory<Workout>(filename,testClassLoader) {

    override fun produceHashMapOfT(tList: List<Workout>): HashMap<String, Workout> {
        val map = HashMap<String, Workout>()
        for(workout in tList){
            map.put(workout.idWorkout, workout)
        }
        return map
    }
}