package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.util.DateUtil

const val FORCE_NEW_EXERCISESET_EXCEPTION = "FORCE_NEW_EXERCISESET_EXCEPTION"
const val FORCE_NEW_EXERCISESETS_EXCEPTION = "FORCE_NEW_EXERCISESETS_EXCEPTION"
const val FORCE_UPDATE_EXERCISESET_EXCEPTION = "FORCE_UPDATE_EXERCISESET_EXCEPTION"
const val FORCE_DELETE_EXERCISESET_EXCEPTION = "FORCE_DELETE_EXERCISESET_EXCEPTION"

class FakeExerciseSetCacheDataSourceImpl(
    private val exerciseDatas : HashMap<String, Exercise>,
    private val dateUtil: DateUtil
) : ExerciseSetCacheDataSource{

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet,idExercise: String): Long {
        if(exerciseSet.idExerciseSet.equals(FORCE_NEW_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong inserting exercise Set")
        }
        if(exerciseSet.idExerciseSet.equals(FORCE_NEW_EXERCISESETS_EXCEPTION)){
            throw Exception("Something went wrong inserting exercise Sets")
        }
        if(exerciseSet.idExerciseSet.equals(FORCE_GENERAL_FAILURE)){
            return -1
        }


        val listExerciseSet : ArrayList<ExerciseSet> = ArrayList()
        exerciseDatas[idExercise]?.sets?.let{ list ->
            for( set in list){
                listExerciseSet.add(set)
            }
        }

        listExerciseSet.add(exerciseSet)
        exerciseDatas[idExercise]?.sets = listExerciseSet

        return 1
    }

    override suspend fun updateExerciseSet(
        primaryKey: String,
        reps: Int,
        weight: Int,
        time: Int,
        restTime: Int,
        idExercise: String
    ): Int {

        if(primaryKey.equals(FORCE_UPDATE_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong updating exercise Set")
        }

        //Get Set for create date
        var exerciseSet :ExerciseSet? = null
        exerciseDatas[idExercise]?.sets?.let {
            for(set in it){
                if(set.idExerciseSet == primaryKey) {
                    exerciseSet = set
                    break
                }else{
                    return -1
                }
            }
        }

        //Update it
        val updatedExerciseSet = ExerciseSet(
            idExerciseSet = primaryKey,
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            started_at = null,
            ended_at = null,
            created_at = exerciseSet?.created_at ?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )

        //Change list
        val listExerciseSet : ArrayList<ExerciseSet> = ArrayList()
        return exerciseDatas[idExercise]?.sets?.let{ list ->
            for( set in list){
                if( set.idExerciseSet == primaryKey)
                    listExerciseSet.add(updatedExerciseSet)
                else
                    listExerciseSet.add(set)
            }
            exerciseDatas[idExercise]?.sets = listExerciseSet
            1
        }?: -1
    }

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise: String): Int {
        if(primaryKey.equals(FORCE_DELETE_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong deleting exercise Set")
        }

        val listExerciseSet : ArrayList<ExerciseSet> = ArrayList()
        return exerciseDatas[idExercise]?.sets?.let{ list ->
            for( set in list){
                if( set.idExerciseSet != primaryKey)
                    listExerciseSet.add(set)
            }
            exerciseDatas[idExercise]?.sets = listExerciseSet

            if(listExerciseSet.size < list.size) 1 else -1

        }?: -1

    }

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? {
        var exerciseSet : ExerciseSet? = null
        exerciseDatas[idExercise]?.sets?.let { list ->
            for(set in list){
                if(set.idExerciseSet == primaryKey)
                    exerciseSet = set
            }
        }
        return exerciseSet
    }

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet>? {
        return exerciseDatas[idExercise]?.sets
    }

    override suspend fun getTotalExercisesSetByExercise(
        idExercise: String
    ): Int {
       return exerciseDatas[idExercise]?.sets?.let {
            it.size
        }?:-1
    }

}