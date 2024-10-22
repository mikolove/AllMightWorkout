package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.network.firebase.model.WorkoutNetworkEntity
import com.mikolove.core.network.util.toFirebaseTimestamp
import com.mikolove.core.network.util.toZoneDateTime


fun WorkoutNetworkEntity.toWorkout(exercises : List<Exercise>, groups : List<Group>) : Workout{
    return Workout(
        idWorkout = this.idWorkout,
        name = this.name,
        isActive = this.isActive,
        exercises = exercises,
        groups = groups,
        createdAt = this.createdAt.toZoneDateTime(),
        updatedAt = this.updatedAt.toZoneDateTime(),
    )
}

fun Workout.toWorkoutNetworkEntity() : WorkoutNetworkEntity{
    return WorkoutNetworkEntity(
        idWorkout = this.idWorkout,
        name = this.name,
        exerciseIds = this.exercises.mapIndexed{_, exercise -> exercise.idExercise},
        groupIds = this.groups.mapIndexed{_, group -> group.idGroup},
        isActive = this.isActive,
        createdAt = this.createdAt.toFirebaseTimestamp(),
        updatedAt = this.updatedAt.toFirebaseTimestamp()
    )
}