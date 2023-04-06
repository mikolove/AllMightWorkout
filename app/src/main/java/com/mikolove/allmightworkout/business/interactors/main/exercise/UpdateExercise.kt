package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Cache

class UpdateExercise(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

/*    fun updateExercise(
        exercise : Exercise,
    ): Flow<DataState<Long>?> = flow{

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.updateExercise(
                primaryKey = exercise.idExercise,
                name = exercise.name,
                bodyPart = exercise.bodyPart,
                isActive = exercise.isActive,
                exerciseType = exercise.exerciseType.type,
                updatedAt = exercise.updatedAt
            )
        }

        val response = object  : CacheResponseHandler<ExerciseViewState,Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ExerciseViewState>? {
                return if(resultObj>0){
                    DataState.data(
                        response = Response(
                            message = UPDATE_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }else{
                    DataState.data(
                        response = Response(
                            message = UPDATE_EXERCISE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(response)

        updateNetwork(response?.message?.response?.message,exercise)
    }*/

    fun execute(
        exercise : Exercise,
    ) : Flow<DataState<Int>?> = flow{

        emit(DataState.loading<Int>())


        //Get cached exercise
        val cacheResultExercise = safeCacheCall(IO){
            exerciseCacheDataSource.getExerciseById(exercise.idExercise)
        }

        val responseExercise = object : CacheResponseHandler<Exercise,Exercise>(
            response = cacheResultExercise
        ){
            override suspend fun handleSuccess(resultObj: Exercise): DataState<Exercise> {
                return DataState.data(
                    data = resultObj
                )
            }
        }.getResult()

        responseExercise?.message?.let { message ->
            if(message.messageType is MessageType.Error){
                emit(DataState.error<Int>(message = message))
                return@flow
            }
        }

        var cachedExercise : Exercise? = responseExercise?.data
        /*if(cachedExercise == null){
            return@flow
        }*/

        //Update the exercise values
        val cacheResultUpdateExercise = safeCacheCall(IO){
            exerciseCacheDataSource.updateExercise(
                primaryKey = exercise.idExercise,
                name = exercise.name,
                bodyPart = exercise.bodyPart,
                isActive = exercise.isActive,
                exerciseType = exercise.exerciseType.name,
                updatedAt = exercise.updatedAt
            )
        }

        val responseUpdateExercise = object  : CacheResponseHandler<Int,Int>(
            response = cacheResultUpdateExercise,
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                return if(resultObj>0){
                    DataState.data(
                        data = resultObj,
                    )
                }else{
                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("UpdateExercise.Error")
                            .title("")
                            .description(UPDATE_EXERCISE_FAILED)
                            .uiComponentType(UIComponentType.Toast)
                            .messageType(MessageType.Error)
                    )
                }
            }
        }.getResult()

        if(responseUpdateExercise?.message?.messageType is MessageType.Error){
            emit(responseUpdateExercise)

            //Else deal with sets
        }else{

            var error = false
            var deletedSets = mutableListOf<ExerciseSet>()
            val sets = exercise.sets

            //Update Insert or Delete sets break if any error
            cachedExercise?.sets?.let updateLoop@{ cachedSets ->
                //UPDATE or INSERT set
                sets.forEach { set ->

                    //Update
                    val cacheSet = cachedSets.find { it.idExerciseSet == set.idExerciseSet }
                    if(cacheSet != null && !cacheSet.equals(set)){

                        val cacheUpdateSet = safeCacheCall(IO) {
                            exerciseSetCacheDataSource.updateExerciseSet(
                                primaryKey = set.idExerciseSet,
                                reps = set.reps,
                                weight = set.weight,
                                time = set.time,
                                restTime = set.restTime,
                                order = set.order,
                                updatedAt = set.updatedAt,
                                idExercise = exercise.idExercise
                            )
                        }

                        val responseUpdateSet = object : CacheResponseHandler<Int, Int>(
                            response = cacheUpdateSet,
                        ) {
                            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                                return if (resultObj > 0) {
                                    DataState.data(
                                        data = resultObj
                                    )
                                } else {
                                    error = true
                                    DataState.error(
                                        message = GenericMessageInfo.Builder()
                                            .id("UpdateExercise.Error")
                                            .title("")
                                            .description(UPDATE_EXERCISE_SETS_FAILED)
                                            .uiComponentType(UIComponentType.Toast)
                                            .messageType(MessageType.Error))
                                }
                            }
                        }.getResult()

                        if(responseUpdateSet?.message?.messageType is MessageType.Error){
                            return@updateLoop
                        }

                    }

                    //Insert
                    if(cacheSet == null){

                        val cacheInsertSet= safeCacheCall(IO){
                            exerciseSetCacheDataSource.insertExerciseSet(exerciseSet = set, idExercise = exercise.idExercise)
                        }

                        val responseInsertSet = object :CacheResponseHandler<Long, Long>(
                            response = cacheInsertSet,
                        ){
                            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                                return if(resultObj>0){
                                    DataState.data(data = resultObj)
                                }else{
                                    error = true
                                    DataState.error(
                                        message = GenericMessageInfo.Builder()
                                            .id("UpdateExercise.Error")
                                            .title("")
                                            .description(UPDATE_EXERCISE_SETS_FAILED)
                                            .uiComponentType(UIComponentType.Toast)
                                            .messageType(MessageType.Error))
                                }
                            }
                        }.getResult()

                        if(responseInsertSet?.message?.messageType is MessageType.Error){
                            return@updateLoop
                        }
                    }
                }

                //Delete
                cachedSets.forEach{ setInCache ->

                    if(sets.find{ it.idExerciseSet == setInCache.idExerciseSet} == null ){

                        val cacheDeleteSet = safeCacheCall(IO){
                            exerciseSetCacheDataSource.removeExerciseSetById(setInCache.idExerciseSet,exercise.idExercise)
                        }

                        val reponseDeleteSet = object : CacheResponseHandler<Int,Int>(
                            response = cacheDeleteSet,
                        ){
                            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                                return if(resultObj > 0){
                                    DataState.data(
                                        message = null,
                                        data = resultObj)
                                }else{
                                    error = true
                                    DataState.error(
                                        message = GenericMessageInfo.Builder()
                                            .id("UpdateExercise.Error")
                                            .title("")
                                            .description(UPDATE_EXERCISE_SETS_FAILED)
                                            .uiComponentType(UIComponentType.Toast)
                                            .messageType(MessageType.Error))
                                }
                            }
                        }.getResult()

                        if(reponseDeleteSet?.message?.messageType is MessageType.Error){
                            return@updateLoop
                        }

                        deletedSets.add(setInCache)
                    }
                }
            }

            //If error occured send message and stop process
            if(error){
                emit(
                    DataState.error<Int>(
                        message = GenericMessageInfo.Builder()
                            .id("UpdateExercise.Error")
                            .title("")
                            .description(UPDATE_EXERCISE_SETS_FAILED)
                            .uiComponentType(UIComponentType.Toast)
                            .messageType(MessageType.Error)
                    ))

                //Else send message update success and update network
            }else{
                emit(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("UpdateExercise.Success")
                            .title("")
                            .description(UPDATE_EXERCISE_SUCCESS)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = 1
                    ))

                updateNetwork(exercise,deletedSets)

            }
        }

    }

    private suspend fun updateNetwork(exercise : Exercise, deletedSets : List<ExerciseSet>){

        //Update exercise
        safeApiCall(IO){
            exerciseNetworkDataSource.updateExercise(exercise)
        }

        //Update sets
        safeApiCall(IO){
            exerciseSetNetworkDataSource.updateExerciseSets(exercise.sets,exercise.idExercise)
        }

        //Update deleted sets
        if(deletedSets.isNotEmpty()){
            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertDeletedExerciseSets(deletedSets)
            }
        }
    }

/*    private suspend fun updateNetwork(response: String?, exercise: Exercise){
        if(response.equals(UPDATE_EXERCISE_SUCCESS)){
            safeApiCall(IO){
                exerciseNetworkDataSource.updateExercise(exercise)
            }
        }
    }*/

    companion object{
        val UPDATE_EXERCISE_SUCCESS = "Successfully updated exercise."
        val UPDATE_EXERCISE_SETS_FAILED  = "Failed updated exercise. Error updating sets."
        val UPDATE_EXERCISE_FAILED  = "Failed updated exercise."
    }

}