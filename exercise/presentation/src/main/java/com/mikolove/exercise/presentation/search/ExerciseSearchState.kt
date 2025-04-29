package com.mikolove.exercise.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import com.mikolove.exercise.presentation.model.ExerciseUi

data class ExerciseSearchState(

    val searchQuerySelected : TextFieldState = TextFieldState(),
    var exercises : List<ExerciseUi> = emptyList(),
)