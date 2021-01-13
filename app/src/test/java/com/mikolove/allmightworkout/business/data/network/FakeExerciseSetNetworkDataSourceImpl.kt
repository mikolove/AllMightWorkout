package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet

class FakeExerciseSetNetworkDataSourceImpl(
    private val exerciseDatas : HashMap<String, Exercise>
) : ExerciseSetNetworkDataSource{

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String) {
        val listExerciseSet : ArrayList<ExerciseSet> = ArrayList()
        exerciseDatas[idExercise]?.sets?.let{ list ->
            for( set in list){
                listExerciseSet.add(set)
            }
        }

        listExerciseSet.add(exerciseSet)

        exerciseDatas[idExercise]?.sets = listExerciseSet
    }

    override suspend fun updateExerciseSet(
        primaryKey: String,
        exerciseSet: ExerciseSet,
        idExercise: String
    ) {
        val listExerciseSet : ArrayList<ExerciseSet> = ArrayList()
        exerciseDatas[idExercise]?.sets?.let{ list ->
            for( set in list){
                if( set.idExerciseSet == primaryKey)
                    listExerciseSet.add(exerciseSet)
                else
                    listExerciseSet.add(set)
            }
            exerciseDatas[idExercise]?.sets = listExerciseSet
        }
    }

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise: String)
    {
        val listExerciseSet : ArrayList<ExerciseSet> = ArrayList()
        exerciseDatas[idExercise]?.sets?.let{ list ->
            for( set in list){
                if( set.idExerciseSet != primaryKey)
                    listExerciseSet.add(set)
            }
            exerciseDatas[idExercise]?.sets = listExerciseSet

        }
    }

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? {
        var exerciseSet : ExerciseSet? = null
        exerciseDatas[idExercise]?.sets?.let { list ->
            for( set in list){
                if(set.idExerciseSet == primaryKey)
                    exerciseSet = set
            }
        }
        return exerciseSet
    }

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet>? {
        return exerciseDatas[idExercise]?.sets
    }
}