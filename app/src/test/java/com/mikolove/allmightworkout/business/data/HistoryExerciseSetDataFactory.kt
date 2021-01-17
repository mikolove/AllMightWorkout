package com.mikolove.allmightworkout.business.data

import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet

class HistoryExerciseSetDataFactory(testClassLoader: ClassLoader,
                                    filename: String
) : JsonDataFactory<HistoryExerciseSet>(filename,testClassLoader) {

    override fun produceHashMapOfT(tList: List<HistoryExerciseSet>): HashMap<String, HistoryExerciseSet> {
        val map = HashMap<String, HistoryExerciseSet>()
        for(hExerciseSet in tList){
            map.put(hExerciseSet.idHistoryExerciseSet, hExerciseSet)
        }
        return map
    }
}