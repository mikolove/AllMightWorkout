package com.mikolove.core.interactors.exercise

import com.mikolove.core.domain.cache.CacheResponseHandler

import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertExercise(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val exerciseFactory: ExerciseFactory,
    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource,
    private val exerciseSetFactory : ExerciseSetFactory
) {

  /*  fun execute(
        idExercise: String? = null,
        name: String,
        sets: List<ExerciseSet>?,
        exerciseType: ExerciseType,
        bodyPart: BodyPart?,
        idUser : String
    ) : Flow<DataState<Long>?> = flow {

        emit(DataState.loading())

        val newExercise = exerciseFactory.createExercise(
            idExercise = idExercise ?: UUID.randomUUID().toString(),
            name = name,
            sets = sets,
            bodyPart = bodyPart,
            exerciseType = exerciseType,
            created_at = null
        )

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.insertExercise(newExercise,idUser)
        }

        val response = object : CacheResponseHandler<Long, Long>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return if(resultObj >0 ){

                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("InsertExercise.Success")
                            .title("")
                            .description(INSERT_EXERCISE_SUCCESS)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = resultObj)
                }else{

                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("InsertExercise.Error")
                            .title("")
                            .description(INSERT_EXERCISE_FAILED)
                            .uiComponentType(UIComponentType.Toast)
                            .messageType(MessageType.Error),
                        data = null)
                }
            }
        }.getResult()

        emit(response)

        //updateNetwork(response?.message?.description, newExercise)

    }

    fun executeNew(
        idExercise: String? = null,
        name: String,
        sets: List<ExerciseSet>?,
        exerciseType: ExerciseType,
        bodyPart: BodyPart?,
        idUser : String
    ) : Flow<DataState<Long>?> = flow {

        emit(DataState.loading<Long>())

        //Create exercise
        val newExercise = exerciseFactory.createExercise(
            idExercise = idExercise ?: UUID.randomUUID().toString(),
            name = name,
            sets = sets,
            bodyPart = bodyPart,
            exerciseType = exerciseType,
            created_at = null
        )

        //Insert in cache
        val cacheResultExercise = safeCacheCall(IO){
            exerciseCacheDataSource.insertExercise(newExercise, idUser)
        }

        val responseExercise = object : CacheResponseHandler<Long, Long>(
            response = cacheResultExercise,
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return if(resultObj >0 ){
                    DataState.data(
                        data = resultObj)
                }else{

                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("InsertExercise.Error")
                            .title("")
                            .description(INSERT_EXERCISE_FAILED)
                            .uiComponentType(UIComponentType.Toast)
                            .messageType(MessageType.Error))
                }
            }
        }.getResult()

        //If error emit Message
        if(responseExercise.message?.messageType is MessageType.Error){

            emit(responseExercise)

        }else{

            var errorOnInsert = false

            newExercise.sets.forEach loopSet@{ set ->

                val newExerciseSet = exerciseSetFactory.createExerciseSet(
                    idExerciseSet = set.idExerciseSet,
                    reps = set.reps,
                    weight = set.weight,
                    time = set.time,
                    restTime = set.restTime,
                    order = set.order,
                    created_at = null
                )

                //Insert set
                val cacheResultSet = safeCacheCall(IO){
                    exerciseSetCacheDataSource.insertExerciseSet(exerciseSet = newExerciseSet, idExercise = newExercise.idExercise)
                }

                val cacheResponseSet = object : CacheResponseHandler<Long, Long>(
                    response = cacheResultSet,
                ){
                    override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                        return if(resultObj>0){

                            DataState.data(
                                message = null,
                                data = resultObj)

                        }else{

                            errorOnInsert = true
                            DataState.data(
                                message = GenericMessageInfo.Builder()
                                    .id("InsertExerciseSet.Error")
                                    .title("")
                                    .description(InsertExerciseSet.INSERT_EXERCISE_SET_FAILED)
                                    .uiComponentType(UIComponentType.Toast)
                                    .messageType(MessageType.Error),
                                data = null)

                        }
                    }
                }.getResult()

                //if error break loop
                if(cacheResponseSet.message?.messageType is MessageType.Error){
                    return@loopSet
                }
            }

            if(errorOnInsert){

                //check if error occured if yes delete exercise CASCADE prepare message
                safeCacheCall(IO){
                    exerciseCacheDataSource.removeExerciseById(newExercise.idExercise)
                }

                emit(
                    DataState.error<Long>(
                    message = GenericMessageInfo.Builder()
                        .id("InsertExercise.Error")
                        .title("")
                        .description(INSERT_EXERCISE_SETS_FAILED)
                        .uiComponentType(UIComponentType.Toast)
                        .messageType(MessageType.Error)
                    ))

            //If not prepare message success emit cache response
            }else{

                emit(
                    DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("InsertExercise.Success")
                        .title("")
                        .description(INSERT_EXERCISE_SUCCESS)
                        .uiComponentType(UIComponentType.None)
                        .messageType(MessageType.Success),
                    data = responseExercise.data)
                )

                //Update network
                updateNetwork(newExercise)
            }

        }

    }




    suspend fun updateNetwork(exercise : Exercise){
            safeApiCall(IO){
                exerciseNetworkDataSource.insertExercise(exercise)
            }

            exercise.sets.forEach { set ->
                safeApiCall(IO){
                    exerciseSetNetworkDataSource.insertExerciseSet(set,exercise.idExercise)
                }
            }
    }

    companion object{

        val INSERT_EXERCISE_SUCCESS = "Successfully inserted new exercise."
        val INSERT_EXERCISE_FAILED  = "Failed inserting new exercise. Error during exercise insertion"
        val INSERT_EXERCISE_SETS_FAILED  = "Failed inserting new exercise. Error during set insertion"
    }*/
}