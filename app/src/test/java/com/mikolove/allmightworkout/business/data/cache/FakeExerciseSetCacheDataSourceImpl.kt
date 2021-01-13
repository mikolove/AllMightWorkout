package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import kotlinx.coroutines.processNextEventInCurrentThread

const val FORCE_NEW_EXERCISESET_EXCEPTION = "FORCE_NEW_EXERCISESET_EXCEPTION"
const val FORCE_NEW_EXERCISESETS_EXCEPTION = "FORCE_NEW_EXERCISESETS_EXCEPTION"
const val FORCE_UPDATE_EXERCISESET_EXCEPTION = "FORCE_UPDATE_EXERCISESET_EXCEPTION"
const val FORCE_DELETE_EXERCISESET_EXCEPTION = "FORCE_DELETE_EXERCISESET_EXCEPTION"

class FakeExerciseSetCacheDataSourceImpl(
    private val exerciseSetDatas : HashMap<String,ExerciseSet>,
    private val dateUtil: DateUtil
) : ExerciseSetCacheDataSource{

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long {
        if(exerciseSet.idExerciseSet.equals(FORCE_NEW_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong inserting exercise Set")
        }
        if(exerciseSet.idExerciseSet.equals(FORCE_NEW_EXERCISESETS_EXCEPTION)){
            throw Exception("Something went wrong inserting exercise Sets")
        }
        if(exerciseSet.idExerciseSet.equals(FORCE_GENERAL_FAILURE)){
            return -1
        }
        exerciseSetDatas.put(exerciseSet.idExerciseSet,exerciseSet)
        return 1
    }

    override suspend fun updateExerciseSet(
        primaryKey: String,
        reps: Int,
        weight: Int,
        time: Int,
        restTime: Int
    ): Int {
        if(primaryKey.equals(FORCE_UPDATE_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong updating exercise Set")
        }

        val updatedExerciseSet = ExerciseSet(
            idExerciseSet = primaryKey,
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            created_at = exerciseSetDatas.get(primaryKey)?.created_at ?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )

        return exerciseSetDatas.get(primaryKey)?.let {
            exerciseSetDatas.put(primaryKey,updatedExerciseSet)
            1
        }?: -1
    }

    override suspend fun removeExerciseSetById(primaryKey: String): Int {
        if(primaryKey.equals(FORCE_DELETE_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong deleting exercise Set")
        }

        return exerciseSetDatas.remove(primaryKey)?.let {
            1
        }?: -1
    }


}