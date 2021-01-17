package com.mikolove.allmightworkout.business.data

import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout


class HistoryWorkoutDataFactory(testClassLoader: ClassLoader,
filename: String
) : JsonDataFactory<HistoryWorkout>(filename,testClassLoader) {

    override fun produceHashMapOfT(tList: List<HistoryWorkout>): HashMap<String, HistoryWorkout> {
        val map = HashMap<String, HistoryWorkout>()
        for(hWorkout in tList){
            map.put(hWorkout.idHistoryWorkout, hWorkout)
        }
        return map
    }
}