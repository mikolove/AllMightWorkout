package com.mikolove.allmightworkout.util

import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.workout.presentation.WorkoutCollection
import com.mikolove.workout.presentation.WorkoutTypeFilter
import com.mikolove.workout.presentation.WorkoutListState
import java.util.UUID

/*
    Workout Screen
 */

object FakeData{
    fun getWokoutListState() : WorkoutListState = workoutListState
}

private val workoutType1 : WorkoutType = WorkoutType(
    idWorkoutType = "Abs", name = "Abs",bodyParts = null
)
private val workoutType2 : WorkoutType = WorkoutType(
    idWorkoutType = "Chest", name = "Abs",bodyParts = null
)
private val workoutType3 : WorkoutType = WorkoutType(
    idWorkoutType = "Leg", name = "Leg",bodyParts = null
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
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 1",exercises = null,isActive = true,exerciseIdsUpdatedAt = null,startedAt = null,endedAt = null,groups = null,createdAt="01-02-2024 00:00:00",updatedAt="01-02-2024 00:00:00"),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 2",exercises = null,isActive = true,exerciseIdsUpdatedAt = null,startedAt = null,endedAt = null,groups = null,createdAt="01-02-2024 00:00:00",updatedAt="01-02-2024 00:00:00"),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 3",exercises = null,isActive = true,exerciseIdsUpdatedAt = null,startedAt = null,endedAt = null,groups = null,createdAt="01-02-2024 00:00:00",updatedAt="01-02-2024 00:00:00"),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 4",exercises = null,isActive = true,exerciseIdsUpdatedAt = null,startedAt = null,endedAt = null,groups = null,createdAt="01-02-2024 00:00:00",updatedAt="01-02-2024 00:00:00"),
    Workout(idWorkout = UUID.randomUUID().toString(),name="Workout Name 5",exercises = null,isActive = true,exerciseIdsUpdatedAt = null,startedAt = null,endedAt = null,groups = null,createdAt="01-02-2024 00:00:00",updatedAt="01-02-2024 00:00:00"),
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

private val workoutListState = WorkoutListState(
    listWorkoutTypeFilter = workoutsFilter,
    listWorkoutCollection = workoutCollection
)
