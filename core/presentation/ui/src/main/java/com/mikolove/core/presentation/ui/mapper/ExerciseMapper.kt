package com.mikolove.core.presentation.ui.mapper

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.presentation.ui.util.toUi
import com.mikolove.core.presentation.ui.model.ExerciseSetUi
import com.mikolove.core.presentation.ui.model.ExerciseUi

fun Exercise.toExerciseUi() : ExerciseUi {
    return ExerciseUi(
        idExercise = this.idExercise,
        name = this.name,
        //sets = this.sets.map { it.toExerciseSetUi() },
        bodyPart = this.bodyParts.map { it.toBodyPartUi() },
        exerciseType = this.exerciseType.name,
        isActive = this.isActive,
        createdAt = this.createdAt.toUi(),
        updatedAt = this.updatedAt.toUi()
    )
}

fun ExerciseSet.toExerciseSetUi() : ExerciseSetUi {
    return ExerciseSetUi(
        idExerciseSet = this.idExerciseSet,
        reps = this.reps.toString(),
        weight = this.weight.toString(),
        time = this.time.toString(),
        restTime = this.restTime.toString(),
        createdAt = this.createdAt.toUi(),
        updatedAt = this.updatedAt.toUi()
    )
}