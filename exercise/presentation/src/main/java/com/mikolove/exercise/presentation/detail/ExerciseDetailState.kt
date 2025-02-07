package com.mikolove.exercise.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.presentation.ui.model.BodyPartUi
import com.mikolove.core.presentation.ui.model.ExerciseTypeUi
import com.mikolove.core.presentation.ui.model.WorkoutTypeUi

data class ExerciseDetailState(
    val isDataLoaded : Boolean = false,
    val exerciseId : String? = null,

    val workoutTypes : List<WorkoutTypeUi> = emptyList(),
    val bodyParts : List<BodyPartUi> = emptyList(),
    val exerciseTypes : List<ExerciseTypeUi> = emptyList(),

    val nameSelected: TextFieldState = TextFieldState(),
    val workoutTypeSelected : TextFieldState =TextFieldState(),
    val bodyPartsSelected: SnapshotStateList<BodyPart> = mutableStateListOf(),
    val exerciseTypeSelected : TextFieldState = TextFieldState(),
    val isActiveSelected : Boolean = true,

    val isExerciseValid: Boolean = false,
    val hasExerciseChanged : Boolean = false
)