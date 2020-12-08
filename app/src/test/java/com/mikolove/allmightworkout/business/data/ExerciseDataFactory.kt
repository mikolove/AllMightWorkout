package com.mikolove.allmightworkout.business.data

import com.mikolove.allmightworkout.business.domain.model.Exercise

class ExerciseDataFactory(
    testClassLoader: ClassLoader,
    filename: String
) : JsonDataFactory<Exercise>(filename,testClassLoader) {

    override fun produceHashMapOfT(tList: List<Exercise>): HashMap<String, Exercise> {
        val map = HashMap<String, Exercise>()
        for(exercise in tList){
            map.put(exercise.idExercise, exercise)
        }
        return map
    }
}