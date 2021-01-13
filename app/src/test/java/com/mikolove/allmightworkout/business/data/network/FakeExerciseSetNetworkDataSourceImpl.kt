package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

class FakeExerciseSetNetworkDataSourceImpl(
    private val exerciseSetDatas : HashMap<String, ExerciseSet>
) : ExerciseSetNetworkDataSource{
    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet) {
        exerciseSetDatas.put(exerciseSet.idExerciseSet,exerciseSet)
    }

    override suspend fun updateExerciseSet(primaryKey: String, exerciseSet: ExerciseSet) {
        exerciseSetDatas.put(primaryKey,exerciseSet)
    }

    override suspend fun removeExerciseSetById(primaryKey: String) {
        exerciseSetDatas.remove(primaryKey)
    }

}