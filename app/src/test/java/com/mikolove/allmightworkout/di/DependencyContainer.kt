package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.business.data.*
import com.mikolove.allmightworkout.business.data.cache.FakeBodyPartCacheDataSourceImpl
import com.mikolove.allmightworkout.business.data.cache.FakeExerciseCacheDataSourceImpl
import com.mikolove.allmightworkout.business.data.cache.FakeWorkoutCacheDataSourceImpl
import com.mikolove.allmightworkout.business.data.cache.FakeWorkoutTypeCacheDataSourceImpl
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.data.network.FakeBodyPartNetworkDataSourceImpl
import com.mikolove.allmightworkout.business.data.network.FakeExerciseNetworkDataSourceImpl
import com.mikolove.allmightworkout.business.data.network.FakeWorkoutNetworkDataSourceImpl
import com.mikolove.allmightworkout.business.data.network.FakeWorkoutTypeNetworkDataSourceImpl
import com.mikolove.allmightworkout.business.data.network.abstraction.BodyPartNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutTypeNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.model.WorkoutExercise
import com.mikolove.allmightworkout.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)

    lateinit var workoutNetworkDataSource: WorkoutNetworkDataSource
    lateinit var workoutCacheDataSource: WorkoutCacheDataSource
    lateinit var workoutFactory: WorkoutFactory
    lateinit var workoutDataFactory: WorkoutDataFactory

    lateinit var exerciseNetworkDataSource: ExerciseNetworkDataSource
    lateinit var exerciseCacheDataSource: ExerciseCacheDataSource
    lateinit var exerciseFactory: ExerciseFactory
    lateinit var exerciseDataFactory : ExerciseDataFactory

    lateinit var bodyPartCacheDataSource: BodyPartCacheDataSource
    lateinit var bodyPartNetworkDataSource: BodyPartNetworkDataSource
    lateinit var bodyPartFactory: BodyPartFactory
    lateinit var bodyPartDataFactory: BodyPartDataFactory

    lateinit var workoutTypeCacheDataSource : WorkoutTypeCacheDataSource
    lateinit var workoutTypeNetworkDataSource: WorkoutTypeNetworkDataSource
    lateinit var workoutTypeFactory: WorkoutTypeFactory
    lateinit var workoutTypeDataFactory: WorkoutTypeDataFactory


    lateinit var exerciseSetFactory: ExerciseSetFactory
    lateinit var exerciseSetDataFactory: ExerciseSetDataFactory


    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {

        this.javaClass.classLoader?.let { classLoader ->

            workoutDataFactory = WorkoutDataFactory(testClassLoader = classLoader,filename ="workout_list")
            exerciseDataFactory = ExerciseDataFactory(testClassLoader = classLoader,filename ="exercise_list")
            exerciseSetDataFactory = ExerciseSetDataFactory(testClassLoader = classLoader,filename = "exerciseset_list")
            bodyPartDataFactory = BodyPartDataFactory(testClassLoader = classLoader, filename = "bodypart_list")
            workoutTypeDataFactory = WorkoutTypeDataFactory(testClassLoader = classLoader,filename = "workouttype_list")

        }

        workoutTypeFactory = WorkoutTypeFactory()
        bodyPartFactory = BodyPartFactory(workoutTypeFactory)

        exerciseSetFactory = ExerciseSetFactory(dateUtil)
        exerciseFactory = ExerciseFactory(dateUtil,exerciseSetFactory,bodyPartFactory)

        workoutFactory = WorkoutFactory(dateUtil,exerciseFactory)

        workoutNetworkDataSource = FakeWorkoutNetworkDataSourceImpl(
            workoutsData = workoutDataFactory.produceHashMapOfT(
                workoutDataFactory.produceListOfT(Workout::class.java)
            )
        )
        workoutCacheDataSource = FakeWorkoutCacheDataSourceImpl(
            workoutsData = workoutDataFactory.produceHashMapOfT(
                workoutDataFactory.produceListOfT(Workout::class.java)
            ),
            dateUtil = dateUtil
        )

        exerciseCacheDataSource = FakeExerciseCacheDataSourceImpl(
            exercisesData = exerciseDataFactory.produceHashMapOfT(
                exerciseDataFactory.produceListOfT(Exercise::class.java)
            ),
            workoutsData = workoutDataFactory.produceHashMapOfT(
                workoutDataFactory.produceListOfT(Workout::class.java)
            ),
            dateUtil = dateUtil
        )

        exerciseNetworkDataSource = FakeExerciseNetworkDataSourceImpl(
            exercisesData = exerciseDataFactory.produceHashMapOfT(
                exerciseDataFactory.produceListOfT(Exercise::class.java)
            )
        )

        bodyPartCacheDataSource = FakeBodyPartCacheDataSourceImpl(
            bodyPartsData = bodyPartDataFactory.produceHashMapOfT(
                bodyPartDataFactory.produceListOfT(BodyPart::class.java)
            )
        )

        bodyPartNetworkDataSource = FakeBodyPartNetworkDataSourceImpl(
            bodyPartsData = bodyPartDataFactory.produceHashMapOfT(
                bodyPartDataFactory.produceListOfT(BodyPart::class.java)
            )
        )

        workoutTypeCacheDataSource = FakeWorkoutTypeCacheDataSourceImpl(
            workoutTypeDatas = workoutTypeDataFactory.produceHashMapOfT(
                workoutTypeDataFactory.produceListOfT(WorkoutType::class.java)
            )
        )

        workoutTypeNetworkDataSource = FakeWorkoutTypeNetworkDataSourceImpl(
            workoutTypeDatas = workoutTypeDataFactory.produceHashMapOfT(
                workoutTypeDataFactory.produceListOfT(WorkoutType::class.java)
            )
        )
    }
}