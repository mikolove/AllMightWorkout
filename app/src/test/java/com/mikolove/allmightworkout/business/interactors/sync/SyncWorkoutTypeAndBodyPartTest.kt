package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutTypeNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPartFactory
import com.mikolove.allmightworkout.business.domain.model.WorkoutTypeFactory
import com.mikolove.allmightworkout.di.DependencyContainer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
1. insertOrUpdateNetworkWorkoutTypeAndBodyPartIntoCache()
    Network contains more values and different information
    this should check insert and update case at the same time
    a) theres more values in network than in cache json
    b) perform sync
    c) check if notes in network are inserted in cache
 */
class SyncWorkoutTypeAndBodyPartTest {

    //System in test
    private val syncWorkoutTypesAndBodyPart : SyncWorkoutTypesAndBodyPart

    //Dependencies
    private val dependencyContainer : DependencyContainer

    private val workoutTypeCacheDataSource : WorkoutTypeCacheDataSource
    private val workoutTypeNetworkDataSource : WorkoutTypeNetworkDataSource
    private val bodyPartCacheDataSource : BodyPartCacheDataSource

    private val workoutTypeFactory : WorkoutTypeFactory
    private val bodyPartFactory : BodyPartFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutTypeCacheDataSource = dependencyContainer.workoutTypeCacheDataSource
        workoutTypeNetworkDataSource = dependencyContainer.workoutTypeNetworkDataSource
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        workoutTypeFactory = dependencyContainer.workoutTypeFactory
        bodyPartFactory = dependencyContainer.bodyPartFactory
        syncWorkoutTypesAndBodyPart = SyncWorkoutTypesAndBodyPart(
            workoutTypeCacheDataSource,
            workoutTypeNetworkDataSource,
            bodyPartCacheDataSource
        )
    }

    @Test
    fun insertorUpdateNetworkWorkoutTypeAndBodyPartIntoCache() = runBlocking {

        val networkWorkoutTypes = workoutTypeNetworkDataSource.getAllWorkoutTypes()

        //Sync - json bodyParts in WorkoutTypesData will not be updated only check name
        syncWorkoutTypesAndBodyPart.syncWorkoutTypesAndBodyPart()

        //Check Insert or Update
        for(workoutType in networkWorkoutTypes){

            val cachedWt = workoutTypeCacheDataSource.getWorkoutTypeById(workoutType.idWorkoutType)
            assertTrue{ cachedWt != null }

            //Here we can check bodyPart are updated completly
            cachedWt?.bodyParts?.forEach { bodyPart ->

                val cachedBp = bodyPartCacheDataSource.getBodyPartById(bodyPart.idBodyPart)
                assertTrue{ cachedBp != null }
                assertTrue{ cachedBp == bodyPart }
            }
        }
    }
}