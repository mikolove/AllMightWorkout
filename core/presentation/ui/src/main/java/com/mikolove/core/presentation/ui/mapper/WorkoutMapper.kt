package com.mikolove.core.presentation.ui.mapper

import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.presentation.ui.model.WorkoutUi
import com.mikolove.core.presentation.ui.util.toUi

fun Workout.toWorkoutUi() : WorkoutUi {
    return WorkoutUi(
        idWorkout = this.idWorkout,
        name = this.name,
        exercises = this.exercises.map { it.toExerciseUi() },
        groups = this.groups.map { it.toGroupUi() },
        isActive = this.isActive,
        startedAt = this.startedAt?.toUi(),
        endedAt = this.endedAt?.toUi(),
        createdAt = this.createdAt.toUi(),
        updatedAt = this.updatedAt.toUi()
    )
}