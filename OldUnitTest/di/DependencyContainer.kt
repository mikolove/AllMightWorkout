package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.business.data.*
import com.mikolove.allmightworkout.business.data.cache.*
import com.mikolove.allmightworkout.business.data.network.*
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.util.isUnitTest
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.data.analytics.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.data.analytics.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.data.analytics.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.data.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.bodypart.BodyPartFactory
import com.mikolove.core.data.bodypart.abstraction.BodyPartNetworkDataSource
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.data.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.data.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.data.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.data.workout.abstraction.WorkoutNetworkDataSource
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.data.workouttype.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.WorkoutTypeFactory
import com.mikolove.core.data.workouttype.abstraction.WorkoutTypeNetworkDataSource
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DependencyContainer {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
    val dateFormatDefault = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val dateUtil = DateUtil(dateFormat,dateFormatDefault)

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
    lateinit var workoutNetworkTypeDataFactory: WorkoutTypeDataFactory

    lateinit var exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    lateinit var exerciseSetCacheDataSource: ExerciseSetCacheDataSource
    lateinit var exerciseSetFactory: ExerciseSetFactory
    lateinit var exerciseSetDataFactory: ExerciseSetDataFactory

    lateinit var historyWorkoutNetworkDataSource: HistoryWorkoutNetworkDataSource
    lateinit var historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource
    lateinit var historyWorkoutFactory: HistoryWorkoutFactory
    lateinit var historyWorkoutDataFactory: HistoryWorkoutDataFactory

    lateinit var historyExerciseNetworkDataSource: HistoryExerciseNetworkDataSource
    lateinit var historyExerciseCacheDataSource: HistoryExerciseCacheDataSource
    lateinit var historyExerciseFactory: HistoryExerciseFactory
    lateinit var historyExerciseDataFactory : HistoryExerciseDataFactory

    lateinit var historyExerciseSetNetworkDataSource: HistoryExerciseSetNetworkDataSource
    lateinit var historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource
    lateinit var historyExerciseSetFactory: HistoryExerciseSetFactory
    lateinit var historyExerciseSetDataFactory: HistoryExerciseSetDataFactory

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
            workoutNetworkTypeDataFactory = WorkoutTypeDataFactory(testClassLoader = classLoader,filename = "network_wt_list")
            historyWorkoutDataFactory = HistoryWorkoutDataFactory(testClassLoader = classLoader,filename = "historyworkout_list")
            historyExerciseDataFactory = HistoryExerciseDataFactory(testClassLoader = classLoader,filename = "historyexercise_list")
            historyExerciseSetDataFactory = HistoryExerciseSetDataFactory(testClassLoader = classLoader,filename = "historyexerciseset_list")
        }

        workoutTypeFactory = WorkoutTypeFactory()

        bodyPartFactory = BodyPartFactory()

        exerciseSetFactory = ExerciseSetFactory(dateUtil)

        exerciseFactory = ExerciseFactory(dateUtil)

        workoutFactory = WorkoutFactory(dateUtil)

        historyExerciseSetFactory = HistoryExerciseSetFactory(dateUtil)

        historyExerciseFactory = HistoryExerciseFactory(dateUtil)

        historyWorkoutFactory = HistoryWorkoutFactory(dateUtil)

        workoutNetworkDataSource = FakeWorkoutNetworkDataSourceImpl(
            workoutsData = workoutDataFactory.produceHashMapOfT(
                workoutDataFactory.produceListOfT(Workout::class.java)
            ),
            deletedWorkouts = HashMap()
        )
        workoutCacheDataSource = FakeWorkoutCacheDataSourceImpl(
            workoutsData = workoutDataFactory.produceHashMapOfT(
                workoutDataFactory.produceListOfT(Workout::class.java)
            ),
            dateUtil = dateUtil
        )

        /*  Specific shared base */
        //TODO : Fix this one and function linked in FakeDataSource UpdateExercise will not take sets with them due to ROOM unknown specifics
        /************************************************************************/

        var cacheData = exerciseDataFactory.produceHashMapOfT(
            exerciseDataFactory.produceListOfT(Exercise::class.java)
        )
        var networkData = exerciseDataFactory.produceHashMapOfT(
            exerciseDataFactory.produceListOfT(Exercise::class.java)
        )

        exerciseCacheDataSource = FakeExerciseCacheDataSourceImpl(
            exercisesData = cacheData,
            workoutsData = workoutDataFactory.produceHashMapOfT(
                workoutDataFactory.produceListOfT(Workout::class.java)
            ),
            dateUtil = dateUtil
        )

        exerciseSetCacheDataSource = FakeExerciseSetCacheDataSourceImpl(
            exerciseDatas = cacheData,
            dateUtil = dateUtil
        )

        exerciseNetworkDataSource = FakeExerciseNetworkDataSourceImpl(
            workoutsData = workoutDataFactory.produceHashMapOfT(
                workoutDataFactory.produceListOfT(Workout::class.java)
            ),
            networkData,
            deletedExercises = HashMap()
        )

        exerciseSetNetworkDataSource = FakeExerciseSetNetworkDataSourceImpl(
            exerciseDatas = networkData,
            deletedExerciseSets = HashMap()
        )
        /************************************************************************/

        bodyPartCacheDataSource = FakeBodyPartCacheDataSourceImpl(
            workoutTypesData = workoutTypeDataFactory.produceHashMapOfT(
                workoutTypeDataFactory.produceListOfT(WorkoutType::class.java)
            ),
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
            workoutTypeDatas = workoutNetworkTypeDataFactory.produceHashMapOfT(
                workoutNetworkTypeDataFactory.produceListOfT(WorkoutType::class.java)
            )
        )

        historyWorkoutCacheDataSource = FakeHistoryWorkoutCacheDataSourceImpl(
            historyWorkoutsData = historyWorkoutDataFactory.produceHashMapOfT(
                historyWorkoutDataFactory.produceListOfT(HistoryWorkout::class.java)
            ),
            dateUtil = dateUtil
        )


        historyWorkoutNetworkDataSource = FakeHistoryWorkoutNetworkDataSourceImpl(
            historyWorkoutsData = historyWorkoutDataFactory.produceHashMapOfT(
                historyWorkoutDataFactory.produceListOfT(HistoryWorkout::class.java)
            ),
            dateUtil = dateUtil
        )

        historyExerciseCacheDataSource = FakeHistoryExerciseCacheDataSourceImpl(
            historyWorkoutsData =historyWorkoutDataFactory.produceHashMapOfT(
                historyWorkoutDataFactory.produceListOfT(HistoryWorkout::class.java)
            ),
            historyExercisesData = historyExerciseDataFactory.produceHashMapOfT(
                historyExerciseDataFactory.produceListOfT(HistoryExercise::class.java)
            ),
            dateUtil = dateUtil
        )

        historyExerciseSetCacheDataSource = FakeHistoryExerciseSetCacheDataSourceImpl(
            historyExercisesData = historyExerciseDataFactory.produceHashMapOfT(
                historyExerciseDataFactory.produceListOfT(HistoryExercise::class.java)
            ),
            historyExerciseSetsData = historyExerciseSetDataFactory.produceHashMapOfT(
                historyExerciseSetDataFactory.produceListOfT(HistoryExerciseSet::class.java)
            )
        )

    }
}