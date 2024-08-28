package com.mikolove.core.interactors.workoutinprogress

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.data.analytics.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.core.data.analytics.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.core.data.analytics.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.core.data.workouttype.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workouttype.WorkoutType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import kotlin.collections.ArrayList


class InsertHistory(
    private val historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource,
    private val historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
    private val historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
    private val historyWorkoutNetworkDataSource: HistoryWorkoutNetworkDataSource,
    private val historyExerciseNetworkDataSource: HistoryExerciseNetworkDataSource,
    private val historyExerciseSetNetworkDataSource: HistoryExerciseSetNetworkDataSource,
    private val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource,
    private val historyWorkoutFactory: HistoryWorkoutFactory,
    private val historyExerciseFactory: HistoryExerciseFactory,
    private val historyExerciseSetFactory: HistoryExerciseSetFactory
) {


    //This parameter is bad only used for test should change
    fun execute(
        workout : Workout,
        idHistoryWorkout : String? = null,
        idUser : String
    ) : Flow<DataState<String>?> = flow {

        emit(DataState.loading<String>())

        var listHistoryExerciseSaved : ArrayList<HistoryExercise> = ArrayList()
        var listHistoryExerciseSetSaved : ArrayList<HistoryExerciseSet>

        var cacheResponse : DataState<Long>?

        //Convert Workout into HistoryWorkout
        val historyWorkout = historyWorkoutFactory.createHistoryWorkout(
            idHistoryWorkout = idHistoryWorkout ?: UUID.randomUUID().toString(),
            name = workout.name,
            historyExercises = null,
            started_at = workout.startedAt,
            ended_at = workout.endedAt,
            created_at = null
        )

        //Insert workout
        cacheResponse = insertHistoryWorkout(historyWorkout,idUser)

        //Insert exercise and set
        if(!errorOccurred(cacheResponse)){

            workout.exercises?.forEach exerciseLoop@ { exercise ->

                if(errorOccurred(cacheResponse))
                    return@exerciseLoop

                val workoutType = getWorkoutType(exercise.bodyPart)

                val historyExercise = historyExerciseFactory.createHistoryExercise(
                    idHistoryExercise = null,
                    name = exercise.name,
                    bodyPart = exercise.bodyPart?.name,
                    workoutType = workoutType?.name,
                    exerciseType = exercise.exerciseType.name,
                    historySets = null,
                    startedAt = exercise.startedAt,
                    endedAt = exercise.endedAt,
                    createdAt = null
                )

                cacheResponse = insertHistoryExercise(historyExercise,historyWorkout.idHistoryWorkout)

                listHistoryExerciseSetSaved = ArrayList()
                if(!errorOccurred(cacheResponse)){

                    exercise.sets.forEach setLoop@ { set ->

                        //Stop process
                        if(errorOccurred(cacheResponse)){
                            return@setLoop
                        }

                        val historyExerciseSet = historyExerciseSetFactory.createHistoryExerciseSet(
                            idHistoryExerciseSet = null,
                            reps = set.reps,
                            weight = set.weight,
                            time = set.time,
                            restTime = set.restTime,
                            started_at = set.startedAt,
                            ended_at = set.endedAt,
                            created_at = null
                        )

                        //Save Progress for network
                        listHistoryExerciseSetSaved.add(historyExerciseSet)

                        cacheResponse = insertHistoryExerciseSet(historyExerciseSet,
                            historyExercise.idHistoryExercise)


                    }
                }

                historyExercise.historySets = listHistoryExerciseSetSaved
                listHistoryExerciseSaved.add(historyExercise)

            }
            historyWorkout.historyExercises = listHistoryExerciseSaved


            //Update network
            updateNetworkHistoryWorkout(historyWorkout)

            //Emit success InsertHistory
            emit(
                DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("InsertHistory.Success")
                        .title("Insert history success")
                        .description(INSERT_HISTORY_SUCCESS)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = historyWorkout.idHistoryWorkout,)
            )

        }
        else{
            safeCacheCall(IO){
                historyWorkoutCacheDataSource.deleteHistoryWorkout(historyWorkout.idHistoryWorkout)
            }

            emit(
                DataState.error<String>(
                    message = GenericMessageInfo.Builder()
                        .id("InsertHistory.Failed")
                        .title("Insert history failed")
                        .description(INSERT_HISTORY_FAILED)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.Toast),)
            )
        }
    }

    private suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout, idUser: String): DataState<Long>? {
        val cacheInsertHWorkout = safeCacheCall(IO) {
            historyWorkoutCacheDataSource.insertHistoryWorkout(historyWorkout, idUser)
        }

        return object : CacheResponseHandler<Long, Long>(
            cacheInsertHWorkout
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("")
                        .title("")
                        .description("")
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = resultObj
                )
            }
        }.getResult()
    }

    private suspend fun insertHistoryExercise(
        historyExercise: HistoryExercise,
        idHistoryWorkout: String
    ): DataState<Long>? {

        val cacheInsertExercise = safeCacheCall(IO) {
            historyExerciseCacheDataSource.insertHistoryExercise(historyExercise, idHistoryWorkout)
        }

        return object : CacheResponseHandler<Long, Long>(
            cacheInsertExercise,
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("")
                        .title("")
                        .description("")
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = resultObj
                )
            }
        }.getResult()
    }

    private suspend fun insertHistoryExerciseSet(
        historyExerciseSet: HistoryExerciseSet,
        idHistoryExercise: String
    ): DataState<Long>? {

        val cacheInsertExerciseSet = safeCacheCall(IO) {
            historyExerciseSetCacheDataSource.insertHistoryExerciseSet(
                historyExerciseSet,
                idHistoryExercise
            )
        }

        return object : CacheResponseHandler<Long, Long>(
            cacheInsertExerciseSet,
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("")
                        .title("")
                        .description("")
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = resultObj
                )
            }
        }.getResult()
    }

    private suspend fun getWorkoutType(bodyPart : BodyPart?) : WorkoutType?{

        val cacheResult = safeCacheCall(IO){
            workoutTypeCacheDataSource.getWorkoutTypeBydBodyPartId(bodyPart?.idBodyPart)
        }
        val response = object : CacheResponseHandler<WorkoutType, WorkoutType>(
            cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: WorkoutType): DataState<WorkoutType> {

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("")
                        .title("")
                        .description("")
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = resultObj,
                )
            }
        }.getResult()

        return response.data
    }

    private fun errorOccurred(cacheResponse : DataState<Long>?) : Boolean{
        val inserted = cacheResponse?.data ?: -1
        return inserted <= 0
    }

    private suspend fun updateNetworkHistoryWorkout(historyWorkout : HistoryWorkout){

        safeApiCall(IO){
            historyWorkoutNetworkDataSource.insertHistoryWorkout(historyWorkout = historyWorkout)
        }

        historyWorkout.historyExercises?.forEach {  historyExercise ->

            safeApiCall(IO){
                historyExerciseNetworkDataSource.insertHistoryExercise(
                    historyExercise = historyExercise,
                    idHistoryWorkout = historyWorkout.idHistoryWorkout)
            }

            historyExercise.historySets.forEach{ historyExerciseSet ->
                safeApiCall(IO){
                    historyExerciseSetNetworkDataSource.insertHistoryExerciseSet(
                        historyExerciseSet= historyExerciseSet,
                        historyExerciseId = historyExercise.idHistoryExercise,
                        historyWorkoutId = historyWorkout.idHistoryWorkout)
                }
            }
        }
    }

    companion object{

        const val INSERT_HISTORY_SUCCESS = "Successfully inserted history."
        const val INSERT_HISTORY_FAILED  = "Failed inserting history."
    }
}