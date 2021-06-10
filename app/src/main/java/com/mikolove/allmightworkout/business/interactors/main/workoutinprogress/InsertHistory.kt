package com.mikolove.allmightworkout.business.interactors.main.workoutinprogress

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryWorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressViewState
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


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
    fun insertHistory(
        workout : Workout,
        idHistoryWorkout : String? = null,
        stateEvent : StateEvent
    ) : Flow<DataState<WorkoutInProgressViewState>?> = flow {

        var listHistoryExerciseSaved : ArrayList<HistoryExercise> = ArrayList()
        var listHistoryExerciseSetSaved : ArrayList<HistoryExerciseSet>

        var cacheResponse : DataState<Long>?

        //Convert Workout into HistoryWorkout
        val historyWorkout = historyWorkoutFactory.createHistoryWorkout(
            idHistoryWorkout = idHistoryWorkout,
            name = workout.name,
            historyExercises = null,
            started_at = workout.startedAt,
            ended_at = workout.endedAt,
            created_at = null
        )

        //Insert workout
        cacheResponse = insertHistoryWorkout(historyWorkout)

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
                    started_at = exercise.startedAt,
                    ended_at = exercise.endedAt,
                    created_at = null
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
        }

        if(errorOccurred(cacheResponse)){
            //Delete inserted content if added
            safeCacheCall(IO){
                historyWorkoutCacheDataSource.deleteHistoryWorkout(historyWorkout.idHistoryWorkout)
            }

            //Emit error InsertHistory
            emit(
                DataState.data(
                    response = Response(
                        message = INSERT_HISTORY_FAILED,
                        messageType = MessageType.Error(),
                        uiComponentType = UIComponentType.None()
                    ),
                    data = null,
                    stateEvent = stateEvent
                ) as DataState<WorkoutInProgressViewState>?
            )
        }else{

            //Update network
            updateNetworkHistoryWorkout(historyWorkout)

            //Emit success InsertHistory
            emit(
                DataState.data(
                    response = Response(
                        message = INSERT_HISTORY_SUCCESS,
                        messageType = MessageType.Success(),
                        uiComponentType = UIComponentType.None()
                    ),
                    data = WorkoutInProgressViewState(idHistoryWorkoutInserted = historyWorkout.idHistoryWorkout),
                    stateEvent = stateEvent
                )as DataState<WorkoutInProgressViewState>?
            )
        }

    }

    private suspend fun insertHistoryWorkout(historyWorkout: HistoryWorkout): DataState<Long>? {
        val cacheInsertHWorkout = safeCacheCall(IO) {
            historyWorkoutCacheDataSource.insertHistoryWorkout(historyWorkout)
        }

        return object : CacheResponseHandler<Long, Long>(
            cacheInsertHWorkout,
            null
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
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
            null
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
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
            null
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()
    }

    private suspend fun getWorkoutType(bodyPart : BodyPart?) : WorkoutType?{

        val cacheResult = safeCacheCall(IO){
            workoutTypeCacheDataSource.getWorkoutTypeBydBodyPartId(bodyPart?.idBodyPart)
        }
        val response = object : CacheResponseHandler<WorkoutType,WorkoutType>(
            cacheResult,
            null
        ){
            override suspend fun handleSuccess(resultObj: WorkoutType): DataState<WorkoutType> {

                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data
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

            historyExercise.historySets?.forEach{ historyExerciseSet ->
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