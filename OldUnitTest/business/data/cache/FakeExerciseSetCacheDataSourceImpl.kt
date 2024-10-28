package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.util.DateUtil

const val FORCE_NEW_EXERCISESET_EXCEPTION = "FORCE_NEW_EXERCISESET_EXCEPTION"
const val FORCE_NEW_EXERCISESETS_EXCEPTION = "FORCE_NEW_EXERCISESETS_EXCEPTION"
const val FORCE_UPDATE_EXERCISESET_EXCEPTION = "FORCE_UPDATE_EXERCISESET_EXCEPTION"
const val FORCE_DELETE_EXERCISESET_EXCEPTION = "FORCE_DELETE_EXERCISESET_EXCEPTION"
const val FORCE_GET_EXERCISE_SET_BY_ID_EXERCISE_EXCEPTION = "FORCE_GET_EXERCISE_SET_BY_ID_EXERCISE_EXCEPTION"

class FakeExerciseSetCacheDataSourceImpl(
    private val exerciseDatas: HashMap<String, Exercise>,
    private val dateUtil: DateUtil
) : ExerciseSetCacheDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String): Long {
        if(exerciseSet.idExerciseSet.equals(FORCE_NEW_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong inserting exercise Set")
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
        order: Int,
        updatedAt: String,
        idExercise: String
    ): Int {

        if(primaryKey.equals(FORCE_GENERAL_FAILURE)){
            return -1
        }

        if(primaryKey.equals(FORCE_UPDATE_EXERCISESET_EXCEPTION)){
            throw Exception("Something went wrong updating exercise Set")
        }

        //Get Set for create date
        var exerciseSet : ExerciseSet? = exerciseDatas[idExercise]?.sets?.find { set ->
            set.idExerciseSet == primaryKey
        }?: null

        //Update it
        val updatedExerciseSet = exerciseSet?.copy(
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            order = order,
            updatedAt = updatedAt
        )

        if(updatedExerciseSet == null){
            return -1
        }else{
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

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet> {

        if(idExercise.equals(FORCE_GET_EXERCISE_SET_BY_ID_EXERCISE_EXCEPTION)){
            throw Exception("Something went wrong getting exercise Set")
        }
        return exerciseDatas[idExercise]?.sets ?: ArrayList()
    }

    override suspend fun getTotalExercisesSetByExercise(
        idExercise: String
    ): Int {
       return exerciseDatas[idExercise]?.sets?.let {
            it.size
        }?:-1
    }

    override suspend fun removeExerciseSets(exerciseSetsToRemove: List<ExerciseSet>): Int {

        val listOfSetNotRemoved = ArrayList<ExerciseSet>()
        exerciseDatas.values?.forEach{ exercise ->

            exercise.sets.forEach{ exerciseSet ->
                if(!exerciseSetsToRemove.contains(exerciseSet))
                    listOfSetNotRemoved.add(exerciseSet)
            }
            exercise.sets = listOfSetNotRemoved
        }

        return exerciseSetsToRemove.size
    }
}