package com.mikolove.workout.presentation.upsert

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.presentation.ui.model.GroupUi
import java.time.ZonedDateTime

data class WorkoutUpsertState(
    val workoutId : String = "",
    val groups : List<GroupUi> = emptyList(),

    val originalName : String = "",
    val originalGroups : List<Group> = emptyList(),
    val originalCreatedAt : ZonedDateTime = ZonedDateTime.now(),

    val nameSelected : TextFieldState = TextFieldState(),
    val groupsSelected : SnapshotStateList<Group> = mutableStateListOf(),

    val isWorkoutValid : Boolean = false,
)