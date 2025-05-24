package com.mikolove.allmightworkout.util

import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.workout.presentation.overview.WorkoutState
import java.time.ZonedDateTime
import java.util.UUID

/*
    Workout Screen
 */

/*object FakeData{
    fun getWokoutListState() : WorkoutState = workoutListState
}

private val workoutType1 : WorkoutType = WorkoutType(
    idWorkoutType = "Abs", name = "Abs",bodyParts = listOf()
)
private val workoutType2 : WorkoutType = WorkoutType(
    idWorkoutType = "Chest", name = "Abs",bodyParts = listOf()
)
private val workoutType3 : WorkoutType = WorkoutType(
    idWorkoutType = "Leg", name = "Leg",bodyParts = listOf()
)

private val workoutTypeFilter1 = WorkoutTypeFilter(
    workoutTypeId = UUID.randomUUID().toString(),
    workoutType = workoutType1,
    selected = true
)
private val workoutTypeFilter2 = WorkoutTypeFilter(
    workoutTypeId = UUID.randomUUID().toString(),
    workoutType = workoutType2,
    selected = false
)
private val workoutTypeFilter3 = WorkoutTypeFilter(
    workoutTypeId = UUID.randomUUID().toString(),
    workoutType = workoutType3,
    selected = false
)


private val workouts : List<Workout> = listOf(
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 1",exercises = listOf(),isActive = true,startedAt = null,endedAt = null,groups = listOf(),createdAt= ZonedDateTime.now(),updatedAt=ZonedDateTime.now()),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 2",exercises = listOf(),isActive = true,startedAt = null,endedAt = null,groups = listOf(),createdAt= ZonedDateTime.now(),updatedAt=ZonedDateTime.now()),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 3",exercises = listOf(),isActive = true,startedAt = null,endedAt = null,groups = listOf(),createdAt= ZonedDateTime.now(),updatedAt=ZonedDateTime.now()),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 4",exercises = listOf(),isActive = true,startedAt = null,endedAt = null,groups = listOf(),createdAt= ZonedDateTime.now(),updatedAt=ZonedDateTime.now()),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 5",exercises = listOf(),isActive = true,startedAt = null,endedAt = null,groups = listOf(),createdAt= ZonedDateTime.now(),updatedAt=ZonedDateTime.now()),
)


private val collection1 : WorkoutCollection = WorkoutCollection(
    id = UUID.randomUUID().toString(),
    name = "Collection 1",
    workouts = workouts)

private val collection2 : WorkoutCollection = WorkoutCollection(
    id = UUID.randomUUID().toString(),
    name = "Collection 2",
    workouts = workouts)

private val workoutCollection : List<WorkoutCollection> =  listOf(collection1, collection2)
private val workoutsFilter : List<WorkoutTypeFilter> = listOf(workoutTypeFilter1, workoutTypeFilter2, workoutTypeFilter3)

private val workoutListState = WorkoutState(
    listWorkoutTypeFilter = workoutsFilter,
    listWorkoutCollection = workoutCollection
)
*/