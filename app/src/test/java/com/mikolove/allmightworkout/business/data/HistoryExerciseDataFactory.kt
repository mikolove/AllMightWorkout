package com.mikolove.allmightworkout.business.data

import com.mikolove.allmightworkout.business.domain.model.HistoryExercise

class HistoryExerciseDataFactory(testClassLoader: ClassLoader,
                                 filename: String
) : JsonDataFactory<HistoryExercise>(filename,testClassLoader) {

    override fun produceHashMapOfT(tList: List<HistoryExercise>): HashMap<String, HistoryExercise> {
        val map = HashMap<String, HistoryExercise>()
        for(hExercise in tList){
            map.put(hExercise.idHistoryExercise, hExercise)
        }
        return map
    }
}