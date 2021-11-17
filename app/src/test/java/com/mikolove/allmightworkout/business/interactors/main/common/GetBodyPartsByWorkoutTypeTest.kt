package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GET_BODYPART_BY_WORKOUT_TYPE_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.common.GetBodyPartsByWorkoutType.Companion.GET_BODYPART_BY_WORKOUT_TYPES_NO_RESULT
import com.mikolove.allmightworkout.business.interactors.main.common.GetBodyPartsByWorkoutType.Companion.GET_BODYPART_BY_WORKOUT_TYPES_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.datasource.cache.database.BODYPART_ORDER_BY_ASC_NAME
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


/*
Test cases:
1. getBodyPartsByWorkoutTypeValidId_success_confirmRetrieved()
    a) search for bodypart with specific id
    b) listen for GET_BODYPART_BY_WORKOUT_TYPES_SUCCESS emitted from flow
    c) confirm bodypart was retrieved
    d) confirm bodypart in cache match with bodypart that was retrieved
2. getBodyPartsByWorkoutTypeInvalidId_success_confirmNoResults()
    a) search for bodypart with specific id
    b) listen for CacheErrors.CACHE_DATA_NULL emitted from flow
    c) confirm nothing was retrieved
3. getBodyPartsByWorkoutType_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
 */
@InternalCoroutinesApi
class GetBodyPartsByWorkoutTypeTest {

    //System in test
    private val getBodyPartsByWorkoutType : GetBodyPartsByWorkoutType

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val bodyPartCacheDataSource : BodyPartCacheDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        getBodyPartsByWorkoutType = GetBodyPartsByWorkoutType(
            bodyPartCacheDataSource
        )
    }

    @Test
    fun getBodyPartsByWorkoutTypeValidId_success_confirmRetrieved() = runBlocking {

        val idWorkoutType = "idWorkoutType1"
        var bodyPartsRetrieved : ArrayList<BodyPart>? = null

        //Search it
        getBodyPartsByWorkoutType.getBodyPartsByWorkoutType<WorkoutViewState>(
            idWorkoutType = idWorkoutType,
            stateEvent = GetBodyPartByWorkoutTypeEvent(idWorkoutType = idWorkoutType)
        ).collect( object  : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_BODYPART_BY_WORKOUT_TYPES_SUCCESS
                )

                value?.data?.listBodyPartsByWorkoutTypes?.let { list ->
                    bodyPartsRetrieved = ArrayList(list)
                }
            }
        })

        assertTrue { bodyPartsRetrieved != null }

        val bodyPartsInCache = bodyPartCacheDataSource.getBodyPartsByWorkoutType(idWorkoutType)
        assertTrue(bodyPartsRetrieved?.containsAll(bodyPartsInCache) == true)

    }

    @Test
    fun getBodyPartsByWorkoutTypeInvalidId_success_confirmNoResults() = runBlocking {

        val idWorkoutType = "idWorkoutTypeInvalid"
        var bodyPartsRetrieved : ArrayList<BodyPart>? = null

        //Search it
        getBodyPartsByWorkoutType.getBodyPartsByWorkoutType<WorkoutViewState>(
            idWorkoutType = idWorkoutType,
            stateEvent = GetBodyPartByWorkoutTypeEvent(idWorkoutType = idWorkoutType)
        ).collect( object  : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_BODYPART_BY_WORKOUT_TYPES_NO_RESULT
                )

                value?.data?.listBodyPartsByWorkoutTypes?.let { list ->
                    bodyPartsRetrieved = ArrayList(list)
                }
            }
        })

        assertTrue { bodyPartsRetrieved?.run { size == 0 }?: true }

        val bodyPartsInCache = bodyPartCacheDataSource.getBodyParts(
            "",
            BODYPART_ORDER_BY_ASC_NAME,
            1
        )
        assertTrue(bodyPartsInCache.size > 0)

    }

    @Test
    fun getBodyPartsByWorkoutType_fail_confirmNoResults() = runBlocking {

        val idWorkoutType = FORCE_GET_BODYPART_BY_WORKOUT_TYPE_EXCEPTION
        var bodyPartsRetrieved : ArrayList<BodyPart>? = null

        //Search it
        getBodyPartsByWorkoutType.getBodyPartsByWorkoutType<WorkoutViewState>(
            idWorkoutType = idWorkoutType,
            stateEvent = GetBodyPartByWorkoutTypeEvent(idWorkoutType = idWorkoutType)
        ).collect( object  : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false)

                value?.data?.listBodyPartsByWorkoutTypes?.let { list ->
                    bodyPartsRetrieved = ArrayList(list)
                }
            }
        })

        assertTrue { bodyPartsRetrieved?.run { size == 0 }?: true }

        val bodyPartsInCache = bodyPartCacheDataSource.getBodyParts(
            "",
            BODYPART_ORDER_BY_ASC_NAME,
            1
        )
        assertTrue(bodyPartsInCache.size > 0)
    }
}