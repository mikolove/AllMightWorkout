package com.mikolove.allmightworkout.business.data

import com.mikolove.allmightworkout.business.domain.model.WorkoutType

class WorkoutTypeDataFactory(
    testClassLoader: ClassLoader,
    filename: String
) : JsonDataFactory<WorkoutType>(filename,testClassLoader) {

    override fun produceHashMapOfT(tList: List<WorkoutType>): HashMap<String, WorkoutType> {
        val map = HashMap<String, WorkoutType>()
        for(wt in tList){
            map.put(wt.idWorkoutType, wt)
        }
        return map
    }
}