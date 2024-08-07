package com.mikolove.allmightworkout.business.data

import com.mikolove.core.domain.exercise.ExerciseSet

class ExerciseSetDataFactory(
    testClassLoader: ClassLoader,
    filename : String
) : JsonDataFactory<ExerciseSet>(filename, testClassLoader){
    override fun produceHashMapOfT(tList: List<ExerciseSet>): HashMap<String, ExerciseSet> {
        val map = HashMap<String, ExerciseSet>()
        for(set in tList){
            map.put(set.idExerciseSet,set)
        }
        return map
    }
}